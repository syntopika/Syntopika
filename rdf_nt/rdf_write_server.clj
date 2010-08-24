;;; rdf_nt.rdf_write_server.clj 
;;; -- example of abstract architectural style A: writing RDF to file (using N-triples)
;
; rdf-write-server: Essential Points
;  0 - rdf-statement
;  1 - create-statements (maps formData to rdf-statements,
;      includes formatting as well as business rules).
;  2 - nt-rdf-writer takes all of the variables and appends N-triples to file.
;  3 - port-listener (facilitates input, creates sequence of rdf-statements,
;      wraps function in 1, and HTTP response doc.)" "\n" )
;

(ns ^{:author "Bob Savage" :version "v0.2"}
   rdf-nt.rdf-write-server
   ; imports & dependencies
   ;; not needed (:use [clojure.java.io :only (reader writer)]) ; using slurp and spit instead
   (:import (java.io.FileNotFoundException))
)
;
; 0.1 - rdf-statement creates [:subject :predicate :object]
;
(defn rdf-statement "0 - rdf-statement creates [:subject :predicate :object]"
      [theSubject thePredicate theObject]
      {:subject theSubject :predicate thePredicate :object theObject})

; 0.2 - statementSTRING coerce to n-triple string representation
(defn statementSTRING "Convert an rdf-statement to n-triples (period terminated) string" [s] 
    (str (s :subject) " " (s :predicate) " " (s :object) " ."))
;
; 1 - create-statements (maps formData to a sequence rdf-statement, includes formatting).
;
; 1.1 - initialize-statement-creator
;
(defn initialize-statement-creator
      "Pre-dondition: The calling context must have ensured that we have a fresh file at (re)start, else file corruption can occur. The serviceID used in naming the blank nodes that represent the service event is only guaranteed to be unique within this file. Using a file from a previous run can thus include duplicate serviceIDs for different service events."
      []
      (atom 0))
;
;  1.2 - create-statements
;
;     The statement creator must be initialized before calling create-statements!
;     
;     formData is:
;         customerEMAIL
;         customerNAME
;         serviceCOST
;         serviceDATE
;         barberID
;
;    5 statements to create:
;        1 customerEMAIL + Predicate#name + customerNAME
;        2 customerEMAIL + Predicate#received + _:service
;        3 _:service + Predicate#cost + serviceCOST
;        4 _:service + Predicate#date + serviceDATE
;        5 _:service Predicate#providedBy + barberID
;       (6) (assume we already have all barber names, and can look up from barberID)
;
(defn create-statements "Maps formData to a sequence rdf-statement, includes formatting. Initialize before first use!"
    [statement-creator, formData] 
    ; throw an exception if missing any key
    (if-not (every? (fn [x] (contains? formData x))
                                     [:customerEMAIL
                                      :customerNAME
                                      :serviceCOST
                                      :serviceDATE
                                      :barberID])
        (throw (new java.lang.RuntimeException "Missing expected keys")))

    ; confirm valid statement creator
    (if-not (number? @statement-creator)
        (throw (new java.lang.Exception "Invalid Statement Creator")))
    ;
    ;
    (let [id-num (deref statement-creator)]
      ; increment statemets-created
      (swap! statement-creator + 1)
      ;
      ; construct statements
      ;
      ; statement 1
      [(rdf-statement (str  "<mailto://" (:customerEMAIL formData) ">")
                    (str "<http://www.example.com/barbershop/Predicate#name>")
                    (:customerNAME formData))
      ; statement 2
      (rdf-statement (str "<mailto://" (:customerEMAIL formData) ">")
                    (str "<http://www.example.com/barbershop/Predicate#received>")
                    (str "_:service" id-num))
      ; statement 3
      (rdf-statement (str "_:service" id-num )
                    (str "<http://www.example.com/barbershop/Predicate#cost>")
                    (:serviceCOST formData))
      ; statement 4
      (rdf-statement (str "_:service" id-num )
                    (str "<http://www.example.com/barbershop/Predicate#date>")
                    (:serviceDATE formData))
      ; statement 5
      (rdf-statement (str "_:service" id-num )
                    (str "<http://www.example.com/barbershop/Predicate#providedBy>")
                    (:barberID formData))]
    ))

;
; 2.1 - nt-rdf-writer-clear?
;
(defn nt-rdf-writer-clear? "checks to make sure no existing file exists, else throws exception."
    [fPath]
    (
    ; try reading fPath, which should produce error, in which case we catch and proceed
    ; If we don't get error, we throw RuntimeException: Over-write Protection
    (try
         (slurp fPath)
         (throw (new java.lang.RuntimeException (str "Over-write Protection: Existing File at " fPath)))
      (catch java.io.FileNotFoundException e
         ; safe to proceed - although could add actual write test by writing header w/ date
         (true)))))

;
; 2.2 - nt-rdf-writer: takes all of the rdf statements and appends N-triples to file.
;     Pre-condition: statements is assumed to be a sequence of type RDF statement
;     as has been previously defined. This section just combines them into a
;     string representation (by invoking statementSTRING) that is appended (with line end) to file.
;
;(defn nt-rdf-writer
;  "nt-rdf-writer: takes all of the variables and appends N-triples to file."
;  [fPath statements]
;  (spit fPath (interpolate "\n" (apply statementSTRING statements)))
;)
;
; 3 - TODO -
;  3.A facilitates input of information (by listening on port),
;  3.B creates a sequence of statements out of formData
;  3.C wrap the filewriter function (1).
;  3.D sends HTTP response doc.
