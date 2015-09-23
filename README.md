# SHARON code Manual 
## Introduction ##
SHARON is a simulator of Human Activities, Routines and Needs.

### Run ###
SHARON uses Apache ANT for the build process (http://ant.apache.org/).
You can run the project by simply run ant on the project folder.
Other ANT task are:

* **clean**: clean JAR target and build folder
* **doc**: generate javadoc for the project
* **compile**: compile sources into build folder
* **jar**: create `Sharon_<VERSION>.jar` package
* **run**: run the compiled software

To run the program simply launch ant without task name. 


    $ ant


Results will be produced in `data/ActivityOutput` directory.


## High Level Sharon Configuration ##

The configuration of the higher level layer of Sharon is necessary for the simulation of ADLs schedules.

### Needs ### 	
	
    config/needs.conf

Comma separated file where every line represents a need. 
Every line format is: `<ID>, <Name>, <Growth Rate>, <Optional initial status>`

### ADL ###

    config/act_*.conf
    
The template of the file is the following:    
    
    <#id>,<name>
    Effects
    <E1>, ..., <En>
    Weights
    <w1>, ..., <wn>
    Weekdays
    <d1>, ..., <dn> 
    Weather
    <h1>, <h2>, <h3>
    Theta
    <T1>, ...,<Tn>
    MinDuration
    <min_dur>

Odd lines contain values to be used, and even lines contain human readable titles for documentation.    

* `<#id>,<name>` identify the activity
* `<E1>, ..., <En>` represent the effects of the ADL on the corresponding need (as specified in needs.conf). Must be between 0 and 1.
* `<w1>, ..., <wn>` is the list of the needs' names that trigger the activity
* `<d1>, ..., <dn>` are the probability to perform the activity in each of the 7 weekdays
* `[Experimental] <h1>, <h2>, <h3>` represent the rank dependence on three atmospheric weather conditions
* `<T1>, ...,<Tn>` can be the list of 1440 values of probability (one for each minute of the day), or a single constant value, repeated for the whole day.
* `<min_dur>` is the minimum duration of the ADL, expressed in minutes.  

## Low Level Sharon configuration ##

The configuration of the lower level layer of Sharon is optional and needed for the simulation of sensors activation.
Three main configurations must be provided: the house map, the sensors description and the patterns of sensors activations
 representing the ADLs.

### Environment ###

    config/env/maps.conf
    config/env/sensors.conf
    config/env/places.conf

The folder must contain both `maps.conf`, `sensors.conf`. and `places.conf`. The first represents the house map through binary values
separated by spaces: 0s represent walkable area, 1s represent walls; the final line should contain the scale of the representation in points per meter.

The sensors description should be formatted as follows:
`<Name>,<pos_x>,<pos_y>,<range>,<activation_probability>` where the coordinates should be referred to the origin of the map, 
in centimeters; range is the maximum agent distance (in centimeters) for the device activation. Activation probability
is a double between zero and one. It is possible also to shorten the config using only `<Name>,<pos_x>,<pos_y>`, whereas range and probability are guessed as 0 and 1 respectively.

<!--- Extend Places Explanation -->
The places description should be formatted as follows:
`<Name>,<pos_x>,<pos_y>` where the coordinates should be referred to the origin of the map, in centimeters. IDs are guessed given the row of the respective place


### Execution Pattern ###

    config/patterns/patt_*.conf

Patterns configuration files have all the same syntax, in which every line has the following format:

    <ADL_ID>, <Prob>, <Pattern_ID>, <Patter_Name>, <Place_ID_1>, <Perc_time_1>, ... , <Place_ID_n>, <Perc_time_n>
     
* `<ADL_ID>` holds the ID of the ADL the pattern is associated to;
* [Experimental]`<Prob>` is the probability of choosing that specific pattern among all the patterns available for the ADL (Float [0 1]); 
* `<Pattern_ID>` is the unique pattern ID; 
* `<Patter_Name>` contains the human readable pattern identificator;
* `<Place_ID_1>, <Perc_time_1>, ... , <Place_ID_n>, <Perc_time_n>` are the couples describing the positions of the agent,
 `<Place_ID>` is the ID of the sensor to trigger and `<Perc_time>` (Float [0 1]) the time to be spent in such position with respect 
 to the overall time devoted to the ADL.
