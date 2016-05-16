function user_login(credentials, homepages)
{
	$.ajax({
		type: "POST",
		url: "/app/account/login",
		data: credentials,
		success: function(user)
		{
			window.location.href = homepages[user.home]+"?"+user.username;
		}
	});
}

function groupUser_login(credentials, homepages)
{
	$.ajax({
	    type: "POST",
	    url: "/app/account/groupLogin",
	    data: credentials,
	    success: function(groupUser)
	    {
	    	window.location.href = homepages[groupUser.home]+"?"+groupUser.groupID;
	    }
	 });
}

function user_logout(redirect)
{
	$.ajax({
		type: "POST",
		url: "/app/account/logout",
		success: function()
		{
			window.location.href = "/";
		}
	});
}

function user_get()
{
	return self.location.search.substr(1);
}

function groupID_get()
{
	return self.location.search.substr(1);
}
