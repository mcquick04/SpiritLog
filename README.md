# SpiritLog – Project Outline 


## I. Project Description 

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

## II. Problem Addressing 

A.) Problem Statement 

	1.) Paranormal investigation evidence and information is often scattered across many sources such as phone notes, camera rolls, and multiple evidence folders which can lead to disorganization and missing evidence or information on an investigation. 

B.) Impact 

  	1.) Users may lose track of certain pieces of vital evidence due to lost evidence or disorganized evidence setup 

  	2.) Important session context such as equipment used, background information on the location being investigated, methods used to communicate, and observations during the investigation which could lead to complications when reviewing the evidence gathered in the investigation. 

C.) Proposed Solution  

	1.) SpiritLog can centralize investigation details into a more strcutred investigation log format 

	2.) Each investigation log can be individually reviewed, updated, and even referenced in other investigations when reviewing evidence making long term tracking of investigations easier 


## III. Platform 

A.) Platform Choice 

	1.) Android Mobile Application developed using Android SDK (Software Development Kit)

	2.) Testing will be performed on an Android Emulator such as BlueStacks 

B.) Justification  

	1.) Android SDK (Software Development Kit) allows fast development, simplified testing, and amodern UI, and is also considered one of the easier platforms to use for beginner mobile application developers.  

	2.) Reduces setup complexity while still helping reinforce knowledge on mobile application concepts such as navigation, storage, and application components.  


## IV. Front End / Back End Support 

A.) Front End 

	1.) Android SDK (Software Development Kit) UI using screens, forms, lists, and buttons 

	2.) Stack navigation form common mobile flow such as (Home to Details to Edit) 

	3.) Input validation to reduce occurrences of incomplete or incorrect entries made in the application 

B.) Back End 

	1.) Firebase Firestore will be used as the back end database to store investigation logs 

	2.) Firestore supports operations such as create, read, update, and delete for saving and retrieving investigation logs 

	3.) Data will be stored in a Firestore collection in which the data will be retrieved by the SpiritLog application 


## V. Functionality 

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


## VI. Design (Wireframes) 

A.) Home/Investigations List Screen 

Description -

The Home Screen of SpiritLog serves as the main landing page of the application. It displays a scrollable list of previously saved investigation logs retrieved from Firebase Firestore. Users can tap on an investigation log on the scrollable list to view its details
or select the add button to create a new investigation log entry.

Wireframe - 

+──────────────────────────────────────────────+
│                  SpiritLog                   |         
│______________________________________________|                                              
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│ (entry: date time - name)                    │
│                                              │
│                                              │
│                                              │
│                                      [ + ]   │
│                                              │
+──────────────────────────────────────────────+

B.) New Investigation From Screen 

Description -

The New Investigation screen allows users to create a new paranormal investigation log. Users can enter the investigation title, location, and date & time. The equipment used checklist allows investigators to select equipment used during the investigation. The Evidence & Media section allows the user to upload reference media such as pictures, videos, and audio recordings. A notes field also provides space for users to document observations. Pressing the "Save Entry" button stores the investigation ;pg in Firebase Firestore.

Wireframe - 

+──────────────────────────────────────────────+
│ <-            New Investigation              │
├──────────────────────────────────────────────┤
│                                              │
│  Title:                                      │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│  Location:                                   │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│  Date / Time:                                │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│  Equipment Used:                             │
│  [x] Spirit Box                              │
│  [] EVP Recorder                             │
│  [] Thermometer                              │
│  [x] Audio Recorder                          │
│  [] Motion Sensing Cat Balls                 │
│  [x] Thermal Imager                          │
│                                              │
│  Evidence & Media:                           │                         
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│   [ Picture ]   [ Video ]   [ Audio ]        │
│                                              │
│  Notes:                                      │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  │                                        │  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│              [ Save Entry ]                  │
│                                              │
+──────────────────────────────────────────────+

C.) Investigation Details Screen 

Description - 

The Investigation Details Screen displays a saved investigation log in a read-only format. This screen allows user to review the information recorded during a paranormal investigation. The displayed information includes the investigation's title, location, date & time, equipment used, notes, and any media such as images, videos, audio, or URLs. The layout is organized so that users can easily review the details of the logged investigation. At the bottom of the screen an "Edit Investigation Entry" button is provided which navigates the user to the "Edit Investigation" screen.

Wireframe - 

+──────────────────────────────────────────────+
│ <-         Investigation Details             │
├──────────────────────────────────────────────┤
│            (dispalys title entered)          │
│  											   |
|                                              |
|  Date & Time:                                |
|   (displays entered date & time)             |
│                                              |
│                                              │
│                                              │
│  	                                           |
|  Location:                                   |
|   (displays entered location)                |
│                                              |
│                                              │
│                                              |
│                                              │
│  Equipment Used:                             │
│    (displays checked items)                  │
│                                              │
│                                              │
│                                              │
│                                              │
│                                              │
│                                              │
│  Evidence & Media:                           │                         
│    (displays uploaded image and video url)   | 
|											   |
|                                              |
│                                              │
│                                              │
│                                              │
│  Notes:                                      │
│   (displays entered notes)				   |
|                                              |
|                                              |
|                                              |
|                                              |
│                                              │
│         [ Edit Investigation Entry ]         │
│                                              │
+──────────────────────────────────────────────+

D.) Edit Investigation Screen 

Desciption -

The Edit Investigation screen allows a user to modify existing investigation logs. When the screen is opened, all fields are automatically populated with the previously saved data from Firebase Firestoe. Users can update information such as the investigation title, date & time, equipment used, notes, and media such as images, videos, audio recordings, and URLs. The screen maintains the same layout as the "New Investigation" screen to ensure consistency in the user experience. At the bottom of the screen their are two buttons a "Save" button and "Delete" button. With the "Save" button users can save changes made to the log which will automatically update the log upon saving. The "Delete" button gives the user a pop up asking if the user really wants to delete the entry and if approved the entry will be deleted.  

Wireframe - 

+──────────────────────────────────────────────+
│ <-           Edit Investigation              │
├──────────────────────────────────────────────┤
│                                              │
│  Title:                                      │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│  Location:                                   │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│  Date / Time:                                │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│  Equipment Used:                             │
│  [x] Spirit Box                              │
│  [] EVP Recorder                             │
│  [] Thermometer                              │
│  [x] Audio Recorder                          │
│  [] Motion Sensing Cat Balls                 │
│  [x] Thermal Imager                          │
│                                              │
│  Evidence & Media:                           │                         
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│   [ Picture ]   [ Video ]   [ Audio ]        │
│                                              │
│  Notes:                                      │
│  +────────────────────────────────────────+  │
│  │                                        │  │
│  │                                        │  │
│  │                                        │  │
│  +────────────────────────────────────────+  │
│                                              │
│         [ Save ]          [ Delete ]         │
│                                              │
+──────────────────────────────────────────────+


-----------------------------------------------------

# Version Changelog

## v1.0 - Initial Proposal of SpiritLog Application
	- Defined concept of SpiritLog
	- Created application screen wireframes
	- Defined core features of applcation
	- Identified Application target users
	- Chose application development Android SDK (Software Development Kit), had to switch due to complications with research
	- Chose backend support (Firebase Firestore)

## v.1.1 - Week 4 Project Update 
	- Implemented core UI screens for investigation logging entries
	- Designed input forms (text input) for investigation logging
	- Established screen structure for easier app navigation
	- Prepares application layout for future application screen flow integration
	
	- Planned additions for next update:
		- Design input forms (checkboxes/equipment types)
		- Input Date/Time feature into investigation logging
		- Develop saving/editting/deleting of investigation entries
		- Develop application navigation flow throughout the app
		- Develop image/video evidence storage
		- Prepare integration of Firestore storage 

## v.1.2 - Week 5 Project Update
	-Added validation for entries such as title and date/time
	-Added date/time features into investigation logging
	-Added checkboxes to new entry screen 
	-Added forward navigation throughout the app
	-Added displaying of checked items from saved entry to details screen
	-Added button to save new entries, 
		-Added interactable icon containing location name and date that navigated user to details of interacted entry 
	-Added UI for entry details
	-Added “Edit Investigation” button to Entry Details

	-Planned additions for next update:
		-Add UI for "Edit Investigation"
		-Add functioning "Save" and "Delete" buttons for Edit Investigation 
		-Add video & audio evidence upload capabilities for new entry
		-Begin integration of Firestore storage

## v.1.3 - Week 6 Project Update
	-Added UI for Edit Investigation
	-Added functioning “Save Changes” and “Delete Entry” buttons
	-Added pop-up verifying if user wants to delete entry
	-Changed titles other than Homepage title to ActionBars
		-Added backwards navigation throughout the app with back arrows in ActionBars
	
	-Planned additions for next update:
		-Add complete integration of Firestore storage
		-Add video & audio evidence upload capabilities via phone’s files
		-Touch up overall app UI

## v.1.4 - Final Submission for Class Update (development will continue after class duration)
	-Added full integration of FireStore storage
	-Added unique “documentId” to each entry to ensure that correct entry is edited or deleted when selected
	-Added video, image, and audio media upload capabilities
		-Added an Evidence & Media section with buttons corresponding to images, videos and audio
		-Added field for URL uploading which can be interacted with on details screen
	-Added ProgressBars to show log saving progress of logs
	-Added a swipe refresh feature so logs can be manually refreshed
	-Added offline support for spirit log (for those spot/dead zone investigation areas)
	-Changed UI of home screen to look a bit more clean
		-Removed large add investigation button
		-Added smaller, more clean add investigation button




