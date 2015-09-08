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

To run the program simply launch ant without task name, specifying the number of days to simulate (Default 5).  


    $ ant [-Dnum_days=5]


Results will be produced in `data/ActivityOutput` directory.


## Sharon configuration ##


### Needs Configuration ### 	
	
    config/needs.conf

Comma separated file where every line represents a need. 
Every line format is: `<ID>, <Name>, <Growth Rate>, <Optional initial status>`

### ADL Configuration ###

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
    <T1>, ...,<Tn> | <EDGE>,<HH:MM>,<EDGE><HH:MM>
    MinDuration
    <min_dur>

Odd lines contain values to be used, and even lines contain human readable titles for documentation.    

* `<#id>,<name>` identify the activity
* `<E1>, ..., <En>` represent the effects of the ADL on the corresponding need (as specified in needs.conf). Must be between 0 and 1.
* `<w1>, ..., <wn>` is the list of the needs' names that trigger the activity
* `[Experimental] <d1>, ..., <dn>` are the probability to perform the activity in each of the 7 weekdays
* `[Experimental] <h1>, <h2>, <h3>` represent the rank dependence on three atmospheric weather conditions
* `<T1>, ...,<Tn>` can be the list of 1440 values of probability (one for each minute of the day), or a single constant value, repeated for the whole day. 
Alternatively it is possible to specify `<EDGE>,<HH:MM>,<EDGE><HH:MM>` as rising and falling edges. The implementation makes
 possible to specify step edges (`R` or `F`) or linear slope edges (`RS` - Rising Start - and `RE` - Rising End -, `FS` and `FE`).
 It is also possible to specify mixed edges (e.g. `R,08:00,FS,11:00,FE,12:00`) and multiple peaks (e.g. `R,08:00,F,12:00,R,14:00,F,18:00`)
* `<min_dur>` is the minimum duration of the ADL, expressed in minutes.  
