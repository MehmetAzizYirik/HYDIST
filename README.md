# HydrogenDistributor

Copyright 2018 Mehmet Aziz Yirik

## Introduction

HydrogenDistributor is a implicit hydrogen distributor. For a list of atoms, the algorithm calculates all the possible distributions of the given number of hydrogens.

## Method 

The algorithm relies on a combinatorial problem called "stars and bars" problem from mathematics. For instance, to calculate all the (a,b,c) triplets of the equation 'a+b+c=12', "stars and bars" approach is used. The method consider all the non-negative integer triplets satisfying the equation. To understand "stars and bars" problem in a [theoretical way](https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics)). 

In this chemical problem, the algorithm calculates all the n-tuples (n indicates the number of non hydrogen atoms) for a given number of atoms and hydrogens. In this problem, "stars and bars" methods is restricted by the valences of the atoms. For this class, it takes a [IMolecularFormula] (http://cdk.github.io/cdk/2.2/docs/api/org/openscience/cdk/interfaces/IMolecularFormula.html) as an input and returns a list of [IAtomContainers] (http://cdk.github.io/cdk/2.2/docs/api/org/openscience/cdk/interfaces/IAtomContainer.html) as the output. These atomcontainers are with the set implicit hydrogens- all the possible hydrogen distributions to the atoms.

## Download Source Code

It is assumed that users have git on their system and have initialised their local directory. For more information [set-up-git](https://help.github.com/articles/set-up-git/ )

To download HMD source code:

```
$ git clone https://https://github.com/MehmetAzizYirik/HydrogenDistributor.git
```
## Compiling

To compile HydrogenDistributor, Apache Maven and Java 1.8 (or later) are required.
```
HydrogenDistributor/$ mvn package
```
This command will create jar file named specifically as "jar-with-dependencies" under target folder.

## Usage

HydrogenDistributor.jar can be run from command line with the specified arguments. An example command is given below.

```
java -jar HydrogenDistributor.jar -f C8H10N4O2 -V
```

The definitions of the arguments are given below:

```
usage: java -jar HydrogenDistributor.jar -f <arg> [-v]

For a molecular formula, it calculates all the possible hydrogen
distributions to the atoms.
 -f,--formula <arg>   Molecular Formula (required)
 -v,--verbose         Print messages about the distributor

Please report issues at
https://github.com/MehmetAzizYirik/HydrogenDistributor

```

## Running the Tests

For the HydrogenDistributor class, a test class called test_HydrogenDistributor is built. This test class includes the tests of the main functions. 

## License
This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/MehmetAzizYirik/HydrogenDistributor/blob/master/LICENSE) file for details

## Authors

 - Mehmet Aziz Yirik - [MehmetAzizYirik](https://github.com/MehmetAzizYirik)
 
## Acknowledgements
![YourKit](https://camo.githubusercontent.com/97fa03cac759a772255b93c64ab1c9f76a103681/68747470733a2f2f7777772e796f75726b69742e636f6d2f696d616765732f796b6c6f676f2e706e67)

The developer uses YourKit to profile and optimise code.

YourKit supports open source projects with its full-featured Java Profiler. YourKit, LLC is the creator of YourKit Java Profiler and YourKit .NET Profiler, innovative and intelligent tools for profiling Java and .NET applications.

![cdk](https://github.com/MehmetAzizYirik/HMD/blob/master/cdk.png)

This project relies on the Chemistry Development Project (CDK), hosted under [CDK GitHub](http://cdk.github.io/). Please refer to these pages for updated information and the latest version of the CDK. CDK's API documentation is available though our [Github site](http://cdk.github.io/cdk/).


