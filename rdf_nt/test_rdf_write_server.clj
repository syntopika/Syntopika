;;; rdf_nt/test_rdf_write_server.clj
;;  by Bob Savage <http://syntopika.wordpress.com>
;;
;  This file contains tests of the following:
;  1 - RDF-Statement
;  2 - RDF-Encoding
;  3 - RDF-Output
;

(ns rdf-nt.test-rdf-write-server
  (:use [rdf-nt.rdf-write-server :only (rdf-statement statementSTRING)])
  (:use [clojure.test :only (deftest testing is run-tests)]))

; 1 RDF-Statement
(deftest RDF-Statement-ADT
    (testing "Testing rdf-statement ADT"
    (is (. "A" equals (:subject (rdf-statement "A" "B" "C"))) "Access RDF Component")
    (is (. "A B C ." equals (statementSTRING (rdf-statement "A" "B" "C"))))))

; 2 RDF-Encoding
(deftest Create-Statement
    (testing "Constructing the barbershop triplestore in memory, from a map."
    ; Result should contain every sentance 
))
; 3 RDF-Output

