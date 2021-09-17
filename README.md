# Kodo Backend Application

Kodo is a MOOC platform that offers computing-related courses specifically to address market shifts towards a digital world

## Setup

### Start up

cd to ```/capstone-kodo-backend``` on your terminal and run the following command on ```./startup_kodo.sh```

### Kodo Database

The current application automatically deletes all existing data on every startup. In order to avoid reloading the database, change the ```CONFIG_PROFILE_TYPE``` value to ```prod```

### Stripe

Run the following steps to listen to incoming Stripe events on your local machine

- download ```stripe cli``` on your terminal
- run ```stripe login``` on your terminal
- replace the stripe endpoint secret value in the ```startup_kodo.sh``` file to the temporary endpoint secret given by stripe cli
- run ```stripe listen --forward-to http://localhost:8080/stripe/webhook``` to redirect all stripe events to your local machine

## Production Site

Our production site is hosted on heroku: [https://kodo-capstone-backend.herokuapp.com/](https://kodo-capstone-backend.herokuapp.com/)