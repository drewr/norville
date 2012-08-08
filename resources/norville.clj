{

 :src {:host "localhost"
       :port 10200}

 :dst {:host "localhost"
       :port 9200}

 :handlers [norville.log/wrap-log]  ;; add 'your.handler/fn

}
