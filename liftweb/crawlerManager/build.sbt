name := "Lift 2.5 starter template"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.9.1"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                  "jbcrypt repo" at "http://mvnrepository.com/",
                 "t2v.jp repo" at "http://www.t2v.jp/maven-repo/",
				 "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )

seq(com.github.siasia.WebPlugin.webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-M1"
  Seq(

    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile->defalut",
    "net.liftweb"       %% "lift-mapper"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module" % (liftVersion + "-1.0"),
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "1.12.1"           % "test",
	"mysql"             % "mysql-connector-java" % "5.1.19",
    "ru.circumflex"     % "circumflex-orm"      % "2.1",
    "joda-time"         % "joda-time"           % "1.6.1",
    "com.h2database"    % "h2"                  % "1.3.167"
  )
}

