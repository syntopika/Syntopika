;;; rdf_nt.rdf_write_server.clj -- example of writing RDF to file
;
; rdf-write-server: Essential Points
;  0 - rdf-statement
;  1 - create-statements (maps formData to rdf-statements,
;      includes formatting as well as business rules).
;  2 - nt-rdf-writer takes all of the variables and appends N-triples to file.
;  3 - port-listener (facilitates input, creates sequence of rdf-statements,
;      wraps function in 1, and HTTP response doc.)" "\n" )
;

(ns ^{:author "Bob Savage" :version "v0.0"}
   rdf-nt.rdf-write-server
   ; imports & dependencies
)
;
; 0 - rdf-statement creates [:subject :predicate :object]
;
(defn rdf-statement "0 - rdf-statement creates [:subject :predicate :object]"
      [theSubject thePredicate theObject]
      {:subject theSubject :predicate thePredicate :object theObject})

(defn statementSTRING "Convert an rdf-statement to n-triples (period terminated) string" [s] 
    (str (s :subject) " " (s :predicate) " " (s :object) " ."))
;
; 1 - create-statements (maps formData to a sequence rdf-statement, includes formatting).
;     formData is:
;         customerEMAIL
;         customerNAME
;         serviceCOST
;         serviceDATE
;         barberID
;
;    statements to create:
;         customerEMAIL + Predicate#name + customerNAME
;         customerEMAIL + Predicate#received + _:service
;         _:service + Predicate#cost + serviceCOST
;         _:service + Predicate#date + serviceDATE
;         _:service Predicate#providedBy + barberID
;           (assume we already have all barber names, and can look up from barberID)
;
(defn create-statements "maps formData to a sequence rdf-statement, includes formatting"
    [formData] 
    ; throw an exception if missing any key
    (if-not (every? (fn [x] (contains? formData x))
                                     [:customerEMAIL
                                      :customerNAME
                                      :serviceCOST
                                      :serviceDATE
                                      :barberID])
        (throw (new java.lang.Exception "Missing expected keys")))
    ; construct statements
    [(rdf-statement (str  "<mailto://" (:customerEMAIL formData) ">")
                    (str "<http://www.example.com/barbershop/Predicate#Name>")
                    (:customerNAME formData))
     (rdf-statement (str "<mailto://" (:customerEMAIL formData) ">")
                    (str "<http://www.example.com/barbershop/Predicate#received>")
                    "_:service")]
)  

;
; 2 - nt-rdf-writer: takes all of the rdf statements and appends N-triples to file.
;     Pre-condition: statements is assumed to be a sequence of simple-rdf-statement
;     that has been previously defined. This section just combines them into a
;     string representation that is spit to file.
;(defn nt-rdf-writer
;  "nt-rdf-writer: takes all of the variables and appends N-triples to file."
;  [outPath statements]
;  ;fake body - TODO
;  ; DEBUGG
;  (println (str "outPath: " outPath "."))
;  ;; attempt to write string by interpolating blank and 
;)
;
; 3 - TODO -
;  3.A facilitates input of information (by listening on port),
;  3.B creates a sequence of statements out of formData
;  3.C wrap the filewriter function (1).
;  3.D sends HTTP response doc.
