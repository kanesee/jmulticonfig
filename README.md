# jmulticonfig
Java configuration management for multi-project builds.

There are already lots of great configuration management tools you can use in your project, but a problem arises when your project grows and you build on other components that use the same config manager.

Here's the problem scenario
1) Project A, a standalone app, loads config.xml from the classpath. 
2) Project B, a standalone app, loads config.xml from the classpath.
3) Project B later depends on project A. Uh oh, which config.xml in the classpath will the app load?

One solution is to include all of Project A's config in Project B and somehow make sure to remove Project A's config so it doesn't accidentally get loaded. If Project A is code that you don't control, (e.g. a module built by another department in your company), you may not have an option to remove its config. You may have to just use a completely different config manager or specify the config file if allowed. The first approach leads to loss of standardization of configs. The latter approach requires manually specifying paths, which can get tricky depending on how and where your code is deployed.

jmulticonfig addresses this issue with a simple system of Priorities and Overrides.
The particulars of this solution 
1) config files must follow a naming pattern: config.[priority].properties
e.g. "config.1.properties", "config.2.properties", etc
2) all config files that follow this naming pattern and are in the classpath will be included
3) properties in config files with higher priorities override ones with lower properties
e.g.
Suppose config.1.properties includes the following
foo=1
bar=2
Suppose config.2.properties includes the following
bar=3
In this example, your app will get the properties
foo-1
bar=3

So in our previous example, your solution can simply be
1) Project A includes config.1.properties
2) Project B includes config.2.properties

The benefits
1) Project B will get all of Project A's properties without having to copy-and-paste them
2) Project B can override specific properties from Project A by just including those properties

There can now be determinism in your configuration management.
