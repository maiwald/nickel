(defproject nickel "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring-server "0.4.0"]
                 [reagent "0.6.0-rc"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [compojure "1.5.1"]
                 [hiccup "1.0.5"]
                 [yogthos/config "0.8"]
                 [javax.xml.bind/jaxb-api "2.3.0"]
                 [org.clojure/clojurescript "1.9.229"
                  :scope "provided"]]

  :plugins [[lein-environ "1.0.2"]
            [lein-cljsbuild "1.1.1"]
            [lein-asset-minifier "0.2.7"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler nickel.handler/app
         :uberwar-name "nickel.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "nickel.jar"

  :simple nickel.server

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :cljsbuild
  {:test-commands {"unit" ["phantomjs"
                           "resources/private/js/unit-test.js"
                           "resources/private/html/unit-test.html"]}
   :builds {
            :min {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
                  :compiler {:output-to "target/cljsbuild/public/js/app.js"
                             :output-dir "target/uberjar"
                             :optimizations :advanced
                             :pretty-print  false}}
            :app {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
                  :compiler {:main "nickel.dev"
                             :asset-path "js/out"
                             :output-to "target/cljsbuild/public/js/app.js"
                             :output-dir "target/cljsbuild/public/js/out"
                             :source-map true
                             :optimizations :none
                             :pretty-print  true}}
            :test {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs" "test/cljs"]
                   :compiler {:main "nickel.test"
                              :output-to "target/cljsbuild/public/js/test.js"
                              :optimizations :simple}}}}

  :figwheel {:http-server-root "public"
             :server-port 3449
             :nrepl-port 7002
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["resources/public/css"]
             :ring-handler nickel.handler/app}
  :profiles {:dev
             {:repl-options {:init-ns nickel.repl
                             :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
              :dependencies [[ring/ring-mock "0.3.0"]
                             [ring/ring-devel "1.5.0"]
                             [prone "1.1.1"]
                             [figwheel-sidecar "0.5.6"]
                             [org.clojure/tools.nrepl "0.2.12"]
                             [com.cemerick/piggieback "0.2.1"]
                             [pjstadig/humane-test-output "0.8.0"]]

              :source-paths ["env/dev/clj"]
              :plugins [[lein-figwheel "0.5.6"]]
              :injections [(require 'pjstadig.humane-test-output)
                           (pjstadig.humane-test-output/activate!)]
              :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})
