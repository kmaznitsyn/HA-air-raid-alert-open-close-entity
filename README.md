It is an add-on for Home Assistant server. The main idea is having an safety functionality depending on air raid alert in Ukraine. The safety functionality is implemented as closing covers in Home Assistant server. For tracking there is weather air raid danger or no this app using https://devs.alerts.in.ua/ API. For own usage you should also advertise to the support group and ask for API-token and i recommend you to read a little a bit theirs documentation.

For closing covers this project uses webhooks, that Home Assitant Automatisation providing for users. In application.properties you should override webhooks id for your specific purposes. And also don`t forget about providing a bearer token for accessing Core API within Home Assistant. In order to run this add-on on your Home Assistant server you should copy this repository and paste to the your addons folder using Samba Share for Example. In Future releases could be added more dynamic configuration and flexible usage.
