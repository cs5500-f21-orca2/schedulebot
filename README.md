# :tada:Schedulebot:tada:

## CS5500 - Foundations of Software Engineering
![GitHub Light](https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png#gh-light-mode-onlyy)
### Team Orca
  - Fuen Cui, Yuchi Shi, Hao Zeng, Ziling Chen, Fanxing Yu

### Professor
  - Lash, Alexander

#### Video Introduction
  - [Link to final presentation]()

#### Team's Design Document
  - [Link to team's document folder](https://docs.google.com/document/d/1ovmgM4YWLuRaeZr9wdYqUiz4VMMTyY_pI15BBAdxaYo/edit#heading=h.e9cyjipwvaak)

#### Team's GitHub
  - [Link to team's GitHub](https://github.com/cs5500-f21-orca2/schedulebot)

#### Discord invite link to the team's Discord
  - [Invite link](https://discord.gg/Egc4k5XCsr)

#### Instructions
Schedule Bot is used to assist Professors/TAs/Students with office hours schedule.
We seek to provide students with Professors/TAs’ availability in real-time,
via discord in efforts to promote a more flexible, convenient, and faster scheduling experience
  - TA/Professor 
    - ```/register```:  register as TA/Professor
      - OptionData: Username - NUID - Role(Ta or Professor)
      
    - ```/createOfficeHour```:  Create an office hour session/Create multiple office hour
      - OptionData: Day of the week - Start time - End time
      
    - ```/getScheduleCommand```:  Get your scheduled office hours for the week or a given day (if optionData empty, the entire week is displayed)
      - OptionData: Day of the week
      
    - ```/checkInPersonOfficeHourCommand```:  Get your in-person office hours for the week or a given day (if optionData empty, the entire week is displayed)
      - OptionData: Day of the week
      
    - ```/deleteOfficeHourCommand```:  Delete your office hour if it is not reserved
      - OptionData: Day of the week - Start time - End time
  
  - Student
    - ```/register```:  register as student
      - OptionData: Username - NUID - Role(student)
    
    - ```/getAvailableCommand```:  Get available hours for the week or a given day (if optionData empty, the entire week is displayed)
      - OptionData: Day of the week
      
    - ```/reserveCommand```:  Make a reservation
      - OptionData: Day of the week - start time - end time - Staff name - Office Hour Type
      
    - ```/smptomCommand ```:  Idicate if you are experiencing covid symptom(true if you are experiencing covid symptom; false if you are not)
      - OptionData: covidsymptom
      
    - ```/vaccinateCommand  ```:  Get or set your own vaccination status(true if you are vaccinated or have a waiver, false if you are not)
      - OptionData: vaccinated
      
    - ```/cancelOfficeHourCommand  ```:  Cancel the office hour you reserved
      - OptionData: Day of week - Start time - End time - Staff name
   
#### Code coverage goals
- [x] 65% Line test coverage
- [x] 65% Branch test coverage 
    
#### Bot requirements
- [x] Bot requirement MongoDB
- [x] Fly.io App Servers
- [x] Gradle Build Tool
- [ ] \(Optional) [Deploying with Docker](https://docs.docker.com/get-started/overview/)
  - [ ] \(Optional) Use other database instead of MongoDB
    - [ ] Implement Service interface
    - [ ] Implement GenericRepository interface
    - [ ] Assign GenericRepository<NEUUser> userRepository equal to otherSubclassDBRepository
  - [ ] \(Optional) Use other app servers instead of Fly.io
    - [ ] See fly.toml
    
