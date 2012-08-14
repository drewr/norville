{

 :src {:host "localhost"
       :port 10200}

 :dst {:host "localhost"
       :port 9200}

 :handlers [norville.log/wrap-log
            norville.log/wrap-log-request-sizes]  ;; add 'your.handler/fn

}
