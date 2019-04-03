# HydrogenDistributor

Copyright 2018 Mehmet Aziz Yirik

## Introduction

HydrogenDistributor is a implicit hydrogen distributor. For a list of atoms, the algorithm calculates all the possible distributions of the given number of hydrogens.

## Method 

The algorithm relies on a combinatorial problem called "stars and bars" problem from mathematics. For instance, to calculate all the (a,b,c) triplets of the equation 'a+b+c=12', "stars and bars" approach is used. The method consider all the non-negative integer triplets satisfying the equation. To understand "stars and bars" problem in a [theoretical way](https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics)). This method was also used in a molecular structure generator method from [Hu](https://pdf.sciencedirectassets.com/271374/1-s2.0-S0003267000X08070/1-s2.0-0003267094900442/main.pdf?x-amz-security-token=AgoJb3JpZ2luX2VjEAAaCXVzLWVhc3QtMSJHMEUCIQDl8q1ycXRbO2IyDneMCFX4LV4OO%2BClZ7tv8junYCL23AIgBGYWaps8KBAcmZuNMBW2ee6edTc%2BxR6%2BNXrHv5jz2t4q4wMIuf%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARACGgwwNTkwMDM1NDY4NjUiDBK5uzoacE3Q5%2BmsXiq3AyMmvHAi10xHqn5rbQ33LkAfIgPgle4XwfHjlSwymodbOCXIjo0Bno3LaSw9GAnfxIEmOVxmY3gbEimuaozTlfUAa880ihp4uyzffPZPGGgK5xiwGGDWn1ZrlDUYd79Aswas5Yn1KUDJ0n115ygypTaN5IHwWV%2Fq2nZgRVJrdYQ16%2FBouDxg7ys%2BViKkRIiNG9msSQ8yEvN%2Flrtf%2FbIhjBWRhxQgT6IOz4tTJS8tyk4FDKy8V5QOxWnB69eTFvu1vK9QvT5qEAoxxG2CgD%2F8mjVCUHi1XjAq33ZBzej2dryayJ4HQm9heclkdjai8llXODTCixLME4nHKSdBh1UDeX74jXpH4Btg6IZNq9TF14s3UjlC%2Bsh3JaZZwUEib5E7CEiXx8pQK91Kb%2FUHi7ziWQmDWKGCIujaoXqtoPerup%2Bv5UGoON67u1hNQ7qJ4k%2FtSRvRfXIc6tMqUp4ojMX9qOlxgHWuIzS7mwdXOGiYt4eHi8gliMhno0Fxni0%2FbV4hPBih34DVHtSGqCL5Nf4jYbRF7EZajZRSAJCo9OKzV1GBsr5cbZxfs1t2mrPz%2FX1V4cO8VoAqoUQwg7%2BR5QU6tAHEyyAeZ7oAMC2o0c8SF0jQOpyLsC7sd5gmdd1jBWgB0EDdmbtnlxlzIisXGJfg9PKGpZ8037Wj03LUvlqUcKcJorkujGjihJZYXHwk8fDp37wGCVin2yq%2BjEKCdqNsDdao98wCWkD66nNflQXw5LDr%2Bfb4qkATqm9Udvpz2ZPGTt6WHR7GbHp81M2VN3t77qBj8BJQ3ustULN4vghbxrvNzPbNxKLXl5VjQltpgFANDZdRXik%3D&AWSAccessKeyId=ASIAQ3PHCVTYQJFJ2PVP&Expires=1554280023&Signature=0WMc3UK%2BBICT1K9j8XJ8ig06N7I%3D&hash=183390ec495f34f9b5d624bac26a6eede9b655198dd38b680b658686585bc2da&host=68042c943591013ac2b2430a89b270f6af2c76d8dfd086a07176afe7c76c2c61&pii=0003267094900442&tid=spdf-75ef6d00-1d91-4202-b4b7-54f8716d4059&sid=31484c054d428749d369cf035fcbd73b6670gxrqb&type=client). The first functions of the generator are the interger partitioning used for hydrogen distribution. So, this HydrogenDistributor class is a re-implementation of this study. 

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


