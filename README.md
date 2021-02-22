This library models code metadata.

Currently, two metadata models are provided:

* Code metadata for Java (and Java-like) code (package
  `org.clyze.persistent.model.jvm`). Elements of this metadata provide
  Java-specific information (such as `static` modifiers and class
  supertypes). Example generators of these metadata are the
  [source-ir-fitter
  tool](https://github.com/plast-lab/source-ir-fitter/), [the Doop
  Jimple parser](https://bitbucket.org/yanniss/doop), and the [Doop
  javac plugin](https://bitbucket.org/yanniss/doop-jcplugin/). Doop is
  also a consumer of such metadata, to allow for SARIF integration
  with IDEs.

* Language-agnostic source code metadata that categorize source
  constructs (and provide their location). An example generators of
  these metadata is
  [antlr2datalog](https://github.com/gfour/antlr2datalog/).
