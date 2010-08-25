;;; rdf_nt/test_rdf_write_server.clj
;;  by Bob Savage <http://syntopika.wordpress.com>
;;
;  This file contains tests of the following:
;  1 - RDF-Statement
;  2 - RDF-Encoding
;  3 - RDF-Output
;

(ns rdf-nt.test-rdf-write-server
  (:use [clojure.test :only (deftest testing is)])
  (:use [clojure.java.io :only (file)])
  (:use [rdf-nt.rdf-write-server :only
                                 (rdf-statement
                                  statementSTRING
                                  initialize-statement-creator
                                  create-statements
                                  nt-rdf-writer-clear?
                                  nt-rdf-writer)]))
;; 
;; 1 RDF-Statement
(deftest RDF-Statement-ADT
    (testing "Testing rdf-statement ADT"
    (is (. "A" equals (:subject (rdf-statement "A" "B" "C"))) "Access RDF Component")
    (is (. "A B C ." equals (statementSTRING (rdf-statement "A" "B" "C"))))))
;;
;; 2 RDF-Encoding
(deftest Create-Statements
    (testing "Constructing the barbershop triplestore in memory, from a map."

    (testing "Initialization"
    (is (thrown? Exception
        (create-statements {:customerEMAIL "customerEMAIL"
                            :customerNAME "customerNAME"
                            :serviceCOST "serviceCOST"
                            :serviceDATE "serviceDATE"
                            :barberID "barberID"})
        ) "called without statement-creator")

    (is (= 0 @(initialize-statement-creator)) "initialization returns brand new statement-creator"))
    
    (testing "Statement Creation"
    (is (thrown? RuntimeException 
        (create-statements {:useless "useless key data"}))
        "Bad formDATA (missing keys) should throw Exception.")

    (testing "state update sanity"
        (let [sc (initialize-statement-creator)] 
          (is (= 0 @sc) "brand new")
          (create-statements sc
                           {:customerEMAIL "customerEMAIL"
                            :customerNAME "customerNAME"
                            :serviceCOST "serviceCOST"
                            :serviceDATE "serviceDATE"
                            :barberID "barberID"}) 
          (is  (= 1 @sc) "statements created was incremented"))))
    ))

;;
;; 3 RDF-Output
;;
;; Note: writes to /tmp assumes no file named "rdf-writer.tmp" is there
;;
(deftest RDF-Output-to-File
    (testing "RDF-Output to File."
       ;;
       (testing "writer-clear?"
         (is (= true (nt-rdf-writer-clear? "/tmp/rdf-writer.tmp")) "no file, should return true.")
         ; create file first
         (spit "/tmp/rdf-writer.tmp" "content\n")
         (is (thrown? java.lang.RuntimeException
                     (nt-rdf-writer-clear? "/tmp/rdf-writer.tmp"))
            "should throw exception because file exists.")
         ; clean up temp file
         (.delete (file "/tmp/rdf-writer.tmp")))
       ;;
       (testing "use of nt-rdf-writer"
           ; 1. invoke with a list of statements.
           ; 2. confirm that statements are retrievable.
           ; 3. get rid of the file.
           (let [fPath "/tmp/rdf-writer.tmp"
                 sc (initialize-statement-creator)
                 sList (create-statements sc
                           {:customerEMAIL "customerEMAIL"
                            :customerNAME "customerNAME"
                            :serviceCOST "serviceCOST"
                            :serviceDATE "serviceDATE"
                            :barberID "barberID"})]
                
              (testing "1 Feed sList to nt-rdf-writer."
                (nt-rdf-writer fPath sList))
                ;
                (testing "2 Confirm that satements are retrievable."
                 (let [s (slurp fPath)
                       modelFString "<mailto://customerEMAIL> <http://www.example.com/barbershop/Predicate#name> customerNAME .\n<mailto://customerEMAIL> <http://www.example.com/barbershop/Predicate#received> _:service0 .\n_:service0 <http://www.example.com/barbershop/Predicate#cost> serviceCOST .\n_:service0 <http://www.example.com/barbershop/Predicate#date> serviceDATE .\n_:service0 <http://www.example.com/barbershop/Predicate#providedBy> barberID .\n"]
                     (is (= false (.matches s "")) "string should not = the empty string")
                     (is (= true (.matches s modelFString)) "slurped string should match modelFString"))
                ; 3. get rid of the file.
                (let [f (file fPath)]
                     (.delete f))
                )))))

