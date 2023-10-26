# Java Test

The file measurements.json in the resources directory contains a file with a list of measurements on devices.

Devices can use or produce energy (in/out). Devices are grouped in two device groups: group_a and group_b.

Requirements:
1) Read the measurements.json file and store the values in the Measurements object.
2) Write a method that prints the totals for both groups for in and outgoing power.
    - group, direction, power
    - The power total must have 4 decimal digits
    - Methods you write must be unit tested
3) Write a method that outputs a list of all devices, and their max power, ordered by group, direction and power(ascending):
    - Device: deviceId, group, direction, power.max
    - The deviceId must be the UUID without '-' 
    - The max power must have 4 decimal digits
    - Methods you write must be unit tested

Ensure your code can withstand changing requirements.


