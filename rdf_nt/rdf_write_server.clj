;;; rdf_nt.rdf_write_server.clj -- example of writing RDF to file
(ns ^{:author "Bob Savage" :version "v0.0"
;       :doc (str
;" rdf-write-server: Essential Points" "\n"
;"  0 - rdf-statement" "\n" 
;"  1 - create-statement (maps formData to rdf-statement," "\n"
;"      includes formatting as well as business rules)." "\n"
;"  2 - nt-rdf-writer takes all of the variables and appends N-triples to file." "\n"
;"  3 - port-listener (facilitates input, creates sequence of rdf-statements," "\n"
;"      wraps function in 1, and HTTP response doc.)" "\n" )
;
}
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
; 1 - create-statement (maps formData to a sequence rdf-statement, includes formatting).
;     formData is:
;                 customerEMAIL
;                 customerNAME
;                 serviceCOST
;                 serviceDATE
;                 barberNAME
;
;    statements to create:
;                 "<mailto://" + customerEMAIL + ">" Predicate#name + customerNAME
;                 "<mailto://" + customerEMAIL + ">" Predicate#received + "_:" + service
;                 ; service p + serviceCOST
;                 ; service p + serviceDATE
;                 ; service Predicate/providedBy + barber
;                 ; barber Predicate#name + barberNAME
;
(defn create-statement "maps formData to a sequence rdf-statement, includes formatting"
    [formData] 
    ; throw an exception if missing any key
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
