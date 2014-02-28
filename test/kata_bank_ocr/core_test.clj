(ns kata-bank-ocr.core-test
  (:require [clojure.test :refer :all]
            [kata-bank-ocr.core :as core]))

(deftest use-case-1
  (testing "parsing account file"
    (let [file "test/kata_bank_ocr/file1.txt"]
      (is (= '((0 0 0 0 0 0 0 0 0)
               (1 1 1 1 1 1 1 1 1)
               (2 2 2 2 2 2 2 2 2)
               (3 3 3 3 3 3 3 3 3)
               (4 4 4 4 4 4 4 4 4)
               (5 5 5 5 5 5 5 5 5)
               (6 6 6 6 6 6 6 6 6)
               (7 7 7 7 7 7 7 7 7)
               (8 8 8 8 8 8 8 8 8)
               (9 9 9 9 9 9 9 9 9)
               (1 2 3 4 5 6 7 8 9)
               (0 0 0 0 0 0 0 5 1))
             (->> file
                  core/file->account-lines
                  (map core/account-lines->account-number)))))))
