Common:
	Bugs:
		Check when creating a bug and passing an email, a username is returned
		Check the severity starts with a capital letter when calling the set method
		Check that a date instant string passed to set last updated comes out as an expected date format

	Errors:
		Check to throw and expect exceptions (e.g. incorrect URL throws an exception)
		Check that each of the Errors.value equal a string (e..g incorrect url throws exception, which should have Errors.request message)

	Message:
		Check all the endpoints equal a string
	
	Utilities:
		Check we can encrypt and decrypt a password
		Check the JacksonAdapter works

GUI:
	app:
		common:
			Remove bug templates
			Check that the Bug regex works
			Check the createDisplayName works when passing an email (method needs refactoring)
		
		component:
			Check the result of BugComparator comparing two bug objects
			Check BugCounter returns correct count

		theme:
			Check a colour can be converted to hex correctly