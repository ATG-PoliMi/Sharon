# SHARON code Manual 
## Introduction ##
SHARON is a simulator of Human Activities, Routines and Needs. SHARON is coded in Java, originally with a special distro of Eclipse: Eclipse Repast Simphony. After some changes it can be executed by any Java compiler.

## Run ##
SHARON uses Apache ANT for the build process (http://ant.apache.org/).
You can run the project by simply run ant on the project folder.
Oter ANT task are:

* **clean**: clean JAR target and build folder
* **complie**: compile sources into build folder
* **jar**: create Sharon_<VERISON>.jar package
* **run**: run the compiled software




## Code Packages ##
### SHARON: It contains 4 executable classes, these represent the entry point for the application: ###
* **ADLProfileExtractor**: It is used to extract the time profiles from the ARAS data. Parameters available in the top of the class
* **ADLTemporalDescriptionsGenerator**: This class is used to simulate a Time Profile with a minute granularity of 1440.
* **MainHLLL**: This is the main class of the simulator. Parameters are available in the top of the class.
* **MapPrintTest**: it prints, in the Java console, the map structure described as: 0: floor, 1: wall, 2: sensor.

### SHARON.DATA: Related to the handling of spatial and temporal data Coordinate: It describes a point on the map. Used both for the actor and  for the target sensors###
* **Day**: This class is used to return the current week day and the weather condition (weather is currently unused)
* **Sensor**: It defines a single sensor. (name or ID, value (0/1), X, Y (coordinates on the map))
* **SensorAbstract**: Abstract class of sensor
* **Station**: (or sensor positioned) contains the time for which the actor needs to stay there
* **Targets**: Contains timings for each LowLevel ADL

### SHARON.ENGINE: Related to the handling of ADLs and Needs ###
* **ADL**: It contains all the parameters used to define an ADL (some of them are currently unused)
* **ADLEffect**: It contains the ADLs effects
* **ADLMatcher**: it is the contact point between the HL and the LL ADL description (it selects which LowLevel profile, for each HighLevel profile must be used)
* **LowLevelADL**: It contains the stations (sensors) definition used during the simulation
* **Needs**: It contains the initial values of each need but also the Getter/Setter (Singleton has been implemented)

###SHARON.ENGINE.THREAD: Related to the handling of the two parallel  threads###
* **ADLQueue**: It contains the queue of ADLs produced at high level
* **HLThread**: (or Producer Thread) contains the procedures used to produce an activities schedule (these activities are stored in ADLQueue)
* **LLThread**: (Or Consumer Thread) contains the procedures used to simulate at Low Level the scheduling. Results are stored in data/SensorOutput

###SHARON.XML: Data which should be imported via an XML file but for implementation necessities are hard-coded.###
* **ADLDB**: contains the definition of the High Level ADLs
* **ADLMatcherDB**: matches the HL ADL with the LL ADL (for each HLADL -> 1 or more LLADL)
* **HomeMap**: it contains the map definition for each wall and for each sensor
* **LLADLDB**: contains the definition of the Low Level ADLs

###UTILS: Support classes###
* **Constants**: it contains all the constants used in SHARON
* **Cumulate Histogram**: it prints on a file histograms
* **Distributions**: it contains the definition of the validation metrics used in SHARON (Bhattacharyya distance, Kullback-Leibler divergence, Earth Mover Distance)
* **Time**: Class used to convert ticks (virtual seconds) into minutes/hours

###UTILS.DIJKSTRA:##
It is an imported package used to simulate path within the house. It has performance problems and can be disabled from MainHLLL.java. References:


http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
http://www.algolist.com/code/java/Dijkstra's_algorithm





### RELEASE NOTES ###

* Training is not yet available

