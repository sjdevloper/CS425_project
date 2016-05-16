window.createAjaxTable = function(options)
{
	// failure wrapper
	var on_fail = function(action, url, item)
	{
    	return function(xhr, status, err)
    	{
			alert("Cannot "+action+" "
       			+JSON.stringify(item)+"@"+url
       			+": "+err.toString());
    	}
	}

	// default loadItems/addItem from url
	if (options.hasOwnProperty("url"))
	{
		options.loadItems = function()
		{
			var url = options.url;
			
			$.ajax({
				url: url,
				cache: false,
			})
			.done(function(data)
			{
				if (options.hasOwnProperty("filterBy"))
					options.data = data.filter(options.filterBy);
				else
					options.data = data;
					
				if (options.hasOwnProperty("orderBy"))
					options.data.sort(function(l, r)
					{
						var ll = l[options.orderBy];
						var rr = r[options.orderBy];
						if (ll < rr) return -1;
						if (ll > rr) return 1;
						return 0;
					});
				
				MyTable.create(options);
			})
			.fail(on_fail("get", url, "all"));
		};
		
		options.addItem = function(item)		
		{
			var url = options.url;
			$.ajax({
				url: url,
				type: "POST",
				data: item
			})
			.done(options.loadItems)
			.fail(on_fail("add", url, item));
		};
	}
	
	// default modifyItem/deleteItem from itemUrl
	if (options.hasOwnProperty("itemUrl"))
	{
		options.modifyItem = function(item)
		{
			var url = options.itemUrl(item);
			$.ajax({
				url: url,
				type: "PUT",
				data: item,
						})
			.done(options.loadItems)
			.fail(on_fail("modify", url, item));
		};
		
		options.deleteItem = function(item)
		{
			var url = options.itemUrl(item);
			$.ajax({
				url: url,
				type: "DELETE",
			})
			.done(options.loadItems)
			.fail(on_fail("delete", url, item));
		};
	}
	
	// default onAdd
	if (!options.hasOwnProperty("onAdd"))
		options.onAdd = function()
		{
			var dialogItem = {};
			
			MyDialogBox.create({
				title: "Add",
				columns: options.columns,
				item: dialogItem,
				onOK: function()
				{
					options.addItem(dialogItem);
				}
			});
		}
		
	// default onModify
	if (!options.hasOwnProperty("onModify"))
		options.onModify = function(item)
		{
			var dialogItem = $.extend({}, item);
			
			MyDialogBox.create({
				title: "Modify",
				columns: options.columns,
				item: dialogItem,
				onOK: function()
				{
					options.modifyItem(dialogItem);
				}
			});
		}

	// default onDelete
	if (!options.hasOwnProperty("onDelete"))
		options.onDelete = function(item)
		{
			options.deleteItem(item);
		}
	
	options.loadItems();
}
