Stuff that's not related to school/research

---- Unity projects ----

Roll a ball 3D - Basic Unity project I made following tutorials. Roll a ball around in a 3d plane,
collecting coins.

Fighting game practice - 2D fighting game that I am messing around with. I'm sure the code is sloppy,
this is mostly just me familiarizing myself with unity 2D and it's myraid of commands. 
So far I have implemented some basic physics, sprite animations that I found on opengameart.org, 
and attempted to make it somewhat responsive.

Stuff that definitely needs improving:
-Implement parent class for player/enemies. A lot of them both have similar functions (EG Attack, Die, etc).
-Lots of if/else statements. Will likely have to use a map of different attacks to their values.
-Research/Implement enemy combat AI.
-Probably some ways to improve efficiency/pretty up the code that are not immediately obvious. 

---- Python projects ----

Exoplanets - Using Data found on Kaggle.com, implemented Logistic Regression w/ Regularization 
to determine whether or not a star has an exoplanet or not. Accuracy is around ~50%, 
so my next step is to try it with a Neural Network. Done by myself

ExoPlanetsWithNN - Same Dataset as above, implemented with a Neural Network with 1 hidden layer,  
w/ L2 Regularization. Accuracy is 99.1%. However, the issue is mainly that in the dataset of 570
stars, only 5 have exoplanets in the Test set. Therefore my accuracy is so high because it predicts
all 570 stars do not have a planet :( - 565/570 = 99.1 (the accuracy). Done by myself.

I now view this as practice implementing Logistic Regression, a Neural Network, Regularization,
and testing Precision & Recall.

Honey Production - Codecademy project. Predicting honey production by 2050 based on a Kaggle dataset.

Breast Cancer - Codecademy project. Predicting whether a patient has breast cancer based on K Neighbors

Hockey vs Soccer - Codecademy project. Predicting whether an email is about hockey or soccer based on
Bayesian theory.

Handwriting - Codecademy project. Predicting what a number is based on an visual input

---- Other Projects ----

Random Quote Generator - Implemented using React/HTML/CSS. Not super pretty but I'm satisfied with the functionality. 
Also has the option to tweet quotes. See: https://codepen.io/arthur-vartanyan/pen/BbwzjG

Online Piano - Implemented using jQuery/HTML/CSS. User presses buttons Q/W/E/R/U/I/O and a sound plays. Some notes about the project:
-Only works with keyboard presses. At some point I should implement on-click functionality.
-Animate/prop time is 50ms. May change, this is purely a design choice. I want it to sound natural so if it's too low then the sound cuts out after user takes their finger off button, however a piano has reverb. If it's too long then users who play too fast will experience issues. Have to find a good middle ground. To see what I mean, try changing the delayTime variable to something like 500 and pressing Q really fast.

See: https://codepen.io/arthur-vartanyan/pen/gEbZPW
