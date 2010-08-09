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
  (:use [rdf-nt.rdf-write-server :only
                                 (rdf-statement
                                  statementSTRING
                                  create-statements)]))
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
    (is (thrown? Exception 
        (create-statements {:useless "useless key data"}))
        "Bad formDATA (missing keys) should throw Exception.")
    ; Result should contain every sentence 
))
;;
;; 3 RDF-Output

