(ns ring.middleware.okta-test
  (:require [clojure.test :refer [deftest testing is]]
            [ring.middleware.okta :refer [wrap-okta]]
            [ring.mock.request :refer [request]]
            [ring.util.response :refer [response]]))

(def okta-home "https://company.okta.com")
(def okta-config "/test-resources/custom-okta-config.xml")

(deftest test-wrap-okta
  (let [default-handler #(response %)]
    (testing ":okta-home option is required"
      (let [handler (wrap-okta default-handler)]
        (is (thrown? IllegalArgumentException
                     (handler (request :get "/"))))))

    (testing "#login"
      (testing "default :okta-config")
      (testing "defined :okta-config"
        (let [handler (wrap-okta default-handler {:okta-home okta-home
                                                  :okta-config okta-config})
              response (handler (request :post "/login"))]
          (is (= (-> response :body :okta-config-location) okta-config)))))))
