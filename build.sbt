scalaVersion in ThisBuild := "2.11.7"


resolvers in ThisBuild ++= Seq(
  "twttr" at "https://maven.twttr.com/"
)

libraryDependencies ++= Seq(
    "com.github.finagle" %% "finch-core" % "0.9.1"
  , "com.twitter" %% "twitter-server" % "1.15.0"
)
