var FieldsBox = React.createClass({
	render: function()
	{
		var columns = this.props.columns;
		var item = this.props.item;
		var onChange = this.props.onChange;
		
		var body = columns.map(function(column)
		{
			if ($.isFunction(column.field))
				return "";
			
			if (column.hasOwnProperty("readonly") && column.readonly)
				return "";
			
			var onFieldChange = function(e)
			{
				onChange(column.field, e.target.value);
			}
			
			return (
				<div className='form-group'>
					<label className='control-label'>
						{column.title}
					</label>
					<input type='text'
						className='form-control'
						value={item[column.field]}
						onChange={onFieldChange} />
				</div>
			);
		});
		
		return <form>{body}</form>;
	}
});
		
var MyDialogBox = React.createClass({
	propTypes:
	{
		title: React.PropTypes.string.isRequired,
		columns: React.PropTypes.array.isRequired,
		item: React.PropTypes.object.isRequired,
		onOK: React.PropTypes.func.isRequired,
		onCancel: React.PropTypes.func.isRequired,
		onChange: React.PropTypes.func.isRequired
	},
	
	render: function()
	{
		return (
			<div className='modal show in'>
			<div className='modal-dialog'>
			<div className='modal-content'>
				<div className='modal-header'>
					<h4 className="modal-title">{this.props.title}</h4>
				</div>
				<div className="modal-body">
					<FieldsBox columns={this.props.columns}
						item={this.props.item}
						onChange={this.props.onChange} />
				</div>
				<div className='modal-footer'>
					<button type='button' className='btn btn-default'
						onClick={this.props.onOK}>OK</button>
					<button type='button' className='btn btn-default'
						onClick={this.props.onCancel}>Cancel</button>
				</div>
			</div>
			</div>
			</div>
		);
	}
});

// export
window.MyDialogBox = MyDialogBox;

window.MyDialogBox.create = function(options)
{
	var node = document.createElement('div');
	
	var backdrop = document.createElement('div');
	backdrop.className = 'modal-backdrop in';
	
	$("body").addClass("modal-open");
	
	document.body.appendChild(node);
	document.body.appendChild(backdrop);
	
	var destroy = function()
	{
		$("body").removeClass("modal-open");

		document.body.removeChild(node);
		document.body.removeChild(backdrop);
	}
	
	var refresh = function()
	{
		ReactDOM.render(
			React.createElement(MyDialogBox, {
				title: options.title,
				columns: options.columns,
				item: options.item,
				onOK: function()
				{
					// process if item is not empty
					for (var k in options.item)
					{
						destroy();
						options.onOK();
						return;
					}

					alert("Cannot process empty item!\r\n"
						+"Click Cancel to exit.");
				},
				onCancel: destroy,
				onChange: function(k, v)
				{
					options.item[k] = v;
					refresh();
				}
			}), node);
	}
	
	refresh();
}
