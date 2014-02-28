(ns kata-bank-ocr.core-test
  (:require [clojure.test :refer :all]
            [kata-bank-ocr.core :as core]))

(deftest user-story-1
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

(deftest user-story-2
  (testing "valid account numbers should check as valid?"
    (is (true?
           (core/valid? '(3 4 5 8 8 2 8 6 5)))))
  (testing "invalid account numbers should not check as valid?"
    (is (false? (core/valid? '(3 4 5 8 8 2 8 6 9))))))

(deftest user-story-3
  (testing "illegible? returns true on illegible data"
    (is (true?
         (core/illegible?
          (core/account-lines->account-number
           '(" _  _  _  _  _  _  _  _  _ "
             "  || || || || || || || || |"
             "|_||_||_||_||_||_||_||_||_|"))))))
  (testing "illegible? returns false on legible data"
    (is (false?
         (core/illegible?
          (core/account-lines->account-number
           '(" _  _  _  _  _  _  _  _  _ "
             "| || || || || || || || || |"
             "|_||_||_||_||_||_||_||_||_|"))))))
  (testing "parsing a file should return the parsed account numbers and their status"
    (let [file "test/kata_bank_ocr/file2.txt"]
      (is (= ["000000000"
              "111111111 ERR"
              "000000051"
              "49006771? ILL"
              "1234?678? ILL"]
             (core/parse-file file))))))
