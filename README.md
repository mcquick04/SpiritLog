SpiritLog – Project Outline 


I. Project Description 

A.) Summary 

  1.) SpiritLog is a mobile application that helps users document their paranormal investigations in a consistent and organized fashion. 

  2.) This app will allow users to record their paranormal investigation sessions with location details, date/time, equipment used, methods used, observations, and evidence links such as photos, videos, or audio without uploading the files to the app 

B.) Target Users 

	1.) Solo paranormal investigators and small investigation teams that need a structured log for their investigations. 

	2.) User’s who want a “digital notebook” of sorts that keeps thier paranormal investigation’s details and evidence all in one place. 

C.) Project Goals 

	1.) Provide a simple workflow that can create, view, edit, and delete investigation logs 

	2.) To improve consistency of documentation across multiple investigation sessions and locations  

	3.) Make the reviewing of past paranormal investigations easier and have a way to view and compare notes from past investigations.  

II. Problem Addressing 

A.) Problem Statement 

	1.) Paranormal investigation evidence and information is often scattered across many sources such as phone notes, camera rolls, and multiple evidence folders which can lead to disorganization and missing evidence or information on an investigation. 

B.) Impact 

  1.) Users may lose track of certain pieces of vital evidence due to lost evidence or disorganized evidence setup 

  2.) Important session context such as equipment used, background information on the location being investigated, methods used to communicate, and observations during the investigation which could lead to complications when reviewing the evidence gathered in the investigation. 

C.) Proposed Solution  

	1.) SpiritLog can centralize investigation details into a more strcutred investigation log format 

	2.) Each investigation log can be individually reviewed, updated, and even referenced in other investigations when reviewing evidence making long term tracking of investigations easier 


III. Platform 

A.) Platform Choice 

	1.) Android Mobile Application developed using React Native with Expo 

	2.) Testing will be performed on an Android Emulator such as BlueStacks 

B.) Justification  

	1.) React Native with Expo allows fast development, simplified testing, and amodern UI, and is also considered one of the easier platforms to use for beginner mobile application developers.  

	2.) Reduces setup complexity while still helping reinforce knowledge on mobile application concepts such as navigation, storage, and application components.  


IV. Front End / Back End Support 

A.) Front End 

	1.) React Native with Expo UI using screens, forms, lists, and buttons 

	2.) Stack navigation form common mobile flow such as (Home to Details to Edit) 

	3.) Input validation to reduce occurrences of incomplete or incorrect entries made in the application 

B.) Back End 

	1.) Firebase Firestore will be used as the back end database to store investigation logs 

	2.) Fire store supports operations such as create, read, update, and delete for saving and retrieving investigation logs 

	3.) Data will be stored in a Firestore collection in which the data will be retrieved by the SpiritLog application 


V. Functionality 

A.) Core Features 

	1.) Create Investigation Log 

		a.) Fields: date/time, location name, participants(if with group) 

		b.) Equipment Checklist (feature common tools such as EMF Reader, Infared Camera, Spirit Box, etc. 

		c.) Notes/observations 

		d.) Evidence references (tour information, stories, etc.) 

		e.) Save entry button thats saves log entry to Firestore 

	2.) View Investigation Logs List 

		a.) Displays all saved investigation logs in a scrollable list 

		b.) Each list item shows date and investigation location name for quick identification 

	3.) View Investigation Details 

		a.) Opens selected log and shows all details in selected log entry 

		b.) Displays equipment used, note, and evidence references 

	4.) Edit Investigation Log 

		a.) Allows users to edit and update any part of an existing log entry 

		b.) Saves updated data back to Firestore 

	5.) Delete Investigation Log 

		a.) Removes a selected log entry from Firestore with a yes or no confirmation prompt.  


VI. Design (Wireframes) 

A.) Home/Investigations List Screen 

  1.) Header with App name and a “+Add New Investigation Log” button. 

  2.) Scrollable list of entered investigations (date, location name). 

  3.) Tap an item to open the details screen. 

B.) New Investigation From Screen 

	1.) Date/time input 

	2.) Location name input 

  3.) Equipment Checklist 

  4.) Notes field. 

  5.) Evidence references field for short notes and video/audio URLs 

  6.) Save button to store log into Firebase Firestore  

C.) Investigation Details Screen 

	1.) Display of date/time, and location name 

	2.) Equipment used (read only) 

	3.) Notes sections (read only) 

	4.) Evidence references (list of small notes, descriptions, URLs etc.) 

	5.) “Edit” and “Delete” buttons 

D.) Edit Investigation Screen 

	1.) Pre-filled fields for date/time, location name, equipment used, notes, and evidences references  

	2.) “Save Changes” button 

	3.) “Cancel” button 
