MinerSafetySystem
=================

A [WearHacks](http://wearhacks.com/) project.

Myo-Estimote Miner Alert System:

#### The Hardware

The MinerSafetySystem utilizes the [Myo](https://www.thalmic.com/en/myo/) by Thalmic Labs & the      [Estimote Beacons](http://estimote.com/).

The Myo is a gesture control armband, which uses the muscle's electrical activity to recognize hand and arm gestures. In addition, it provides vibration based tactile feedback.

The Estimote is a beacon that can provided contextual data such as location, temperature and motion via bluetooth.

Note: Currently the temperature and motion features are not implemented in the SDK.

#### Use Case

The idea behind this project is to alert miners of hazardous gases. When gas levels reach a certain threshold, the Myo of a miner will start pulsating on their arm thus indicating to exit the mine immediately.

Note: The SDK by design requires the Myo to be paired with an Android or iOS phone.

Estimote beacons are placed throughout the mine at a regular interval. We designed the Myo to stop vibrating once the miner passes the last Estimote placed at the entrance of the mine.

Note: Due to the limitations of the Estimote SDK further features could not be implemented, such as the speed of an existing miner between two estimotes.

#### Data Website

Using Google Charts we built a [website] (http://miner-safety-system.herokuapp.com/) that can display various data such as temperature and orientation coming from each Estimote in the mine.




