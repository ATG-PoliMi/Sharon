# SHARON - Simulator of Human Activities, ROutines and Needs

The configuration file for the simulator have to be placed in "/confing" 

There are 4 tipe of configuration file:

- The one that contains the needs of the simulator, with their ids, names, growth rate and , optional, a initiazial value of the status of needs. The name of this file has to be "need.conf" and has to be compiled as the template named "/config/Template/template_need.conf", that follow those rules:
	-Every row represents a need and all values has to be separeted with a tabultation.
	-The first value is the id of the need, it has to be unique and must be an integer.
	-The second value is the name of the need, that has to be a string, that could contains also spaces.
	-The third value is the growth rate of need, that has to be a double value.
	-The forth value is optional and it's the initial value of the status. If it isn't specified, the simulator would put all status to "0.0" except for the one named "tirediness" that is satured to "1.0".

- The ones that cointains the ADL of the simulator with all their parameters. The name of those file has to be "act_ADLNAME.conf" and has to be compiled as the template named "/config/Template/act_template.conf", that follow those rules:
	-The first row must contains the id, that has to be unique, and, separeted by a tabulation, the name of the ADL.
	-The seconds must contains only the string "Effects"
	-The third have to contains all the effects of the ADL to the needs' status placed with the same order of the file "need.conf"
	-The forth must contains only the string "Weights"
	-The fifth must contains all the needs' name that tempt to fulfill the ADL
	-The sixth must contains only the string "Weekdays"
	-The seventh must contains one value for each day of the week
	-The eighth must contains only the string "Weather"
	-The ninth must contains 3 values that represents the weather condictions
	-The tenth must contains only the string "Theta"
	-The eleven must contains one float value, that the simulator will use to create the time dependency, or the entire array of 1440 values.
	-The twelfth must contains only the string "MinDuration"
	-The thirteenth must contains the value that represents the minute duration of the activity

-The one that contains the drift of the needs, it has to be named as "need.drift" and compliled as the template named "/config/Template/need.drift", that follow those rules:
	-Every row represents a need and all values has to be separeted with a tabultation.
	-The first value is the id of the need, it has to be unique and must be an integer.
	-The second value is the mode of the need, that has to be a string.
	-The third value is the time when the drift has to appers, that has to be a long value.
	-The forth value is the new growth rate of need, that is has to be a double value

-The ones that contain the drifts of the ADL, they have to be named as "act_NAMEOFADL.drift" and compliled as the template named "/config/Template/act_template.drift", that follow those rules:
	-The first row must contains the id, that has to be unique, and, separeted by a tabulation, the name of the ADL.
	-The second row must contains the mode of the drift, and, separeted by a tabulation, the time paramethers for that mode.
	-All the other row must be at couple of two, with the first that have to contains the name of the parameters of ADL that has to change, and the seconds with the new values of that parameters.
