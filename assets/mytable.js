var AddButton = React.createClass({
	render: function()
	{
		return (
			<button type='button' className='btn btn-primary'
				onClick={this.props.onClick}>
				{this.props.text}
			</button>
		);
	}
});

var DeleteButton = React.createClass({
	render: function()
	{
		return (
			<button type='button' className='btn btn-danger'
				data-toggle='tooltip' title='Delete'
				onClick={this.props.onClick}>
				<span className='glyphicon glyphicon-minus'></span>
			</button>
		);
	}
});

var ModifyButton = React.createClass({
	render: function()
	{
		return (
			<button type='button' className='btn btn-primary'
				data-toggle='tooltip' title='Modify'				
				onClick={this.props.onClick}>
				<span className='glyphicon glyphicon-pencil'></span>
			</button>
		);
	}
});

var Title = React.createClass({
	render: function()
	{
		if ($.isFunction(this.props.title))
			return this.props.title();

		if (this.props.onAdd == null)
			return <h4>{this.props.title}</h4>;
		
		return 	(
			<h4>
				{this.props.title}
				<span> </span>
				<AddButton onClick={this.props.onAdd}
					text={this.props.addText} />
			</h4>
		);
	}
});

var Header = React.createClass({
	render: function()
	{
		var columns = this.props.columns;
		
		var cols = columns.map(function(column)
		{
			if ($.isFunction(column.title))
				return <th>column.title()</th>;
			else
				return <th>{column.title}</th>;
		});

		return <thead><tr>{cols}</tr></thead>;
	}
});

var Row = React.createClass({
	render: function()
	{
		var columns = this.props.columns;
		var item = this.props.item;

		var cols = columns.map(function(column)
		{
			if ($.isFunction(column.field))
				return <td>{column.field(item)}</td>;
			else
				return <td>{item[column.field]}</td>;
		});
		
		var hasModify = (this.props.onModifyClick != null);
		var hasDelete = (this.props.onDeleteClick != null);

		if (!hasModify && !hasDelete)
			return <tr>{cols}</tr>;
			
		var extra = (
			<td>	
				{hasModify? <ModifyButton
					onClick={this.props.onModifyClick} />: ""}
				<span> </span>
				{hasDelete? <DeleteButton
					onClick={this.props.onDeleteClick} />: ""}
			</td>
		);
			
		return <tr>{cols}{extra}</tr>;
	}
});

var Body = React.createClass({
	render: function()
	{
		var columns = this.props.columns;
		var data = this.props.data;
		var onModify = this.props.onModify;
		var onDelete = this.props.onDelete;
		
		var rows = data.map(function(item)
		{
			var onModifyClick = (onModify != null)?
				function() {onModify(item);}: null;
			var onDeleteClick = (onDelete != null)?
				function() {onDelete(item);}: null;
			
			return <Row item={item} columns={columns}
				onModifyClick={onModifyClick}
				onDeleteClick={onDeleteClick} />;
		});
			
		return <tbody>{rows}</tbody>;
	}
});

var Table = React.createClass({
	render: function()
	{
		return (
			<table className={this.props.tableClass}>
				<Header columns={this.props.columns} />
				<Body data={this.props.data}
					columns={this.props.columns}
					onModify={this.props.onModify}
					onDelete={this.props.onDelete} />
			</table>
		);
	}
});

var MyTable = React.createClass({
	propTypes:
	{
		tableClass: React.PropTypes.string.isRequired,
		title: React.PropTypes.string.isRequired,
		columns: React.PropTypes.array.isRequired,
		data: React.PropTypes.array.isRequired,
		onAdd: React.PropTypes.func,
		addText: React.PropTypes.string,
		onModify: React.PropTypes.func,
		onDelete: React.PropTypes.func
	},

	render: function()
	{
		return (
			<div>
			<Title title={this.props.title}
				onAdd={this.props.onAdd}
				addText={this.props.addText} />
			<Table data={this.props.data}
				tableClass={this.props.tableClass}
				columns={this.props.columns}
				onModify={this.props.onModify}
				onDelete={this.props.onDelete} />
			</div>
		);
	}
});

// export
window.MyTable = MyTable;

window.MyTable.create = function(options)
{
	// default table class
	if (!options.hasOwnProperty("tableClass"))
		options.tableClass = "table table-bordered table-striped";
	
	ReactDOM.render(
		React.createElement(MyTable, {
			title: options.title,
			data: options.data,
			columns: options.columns,
			tableClass: options.tableClass,
			onAdd: options.onAdd,
			addText: options.addText,
			onModify: options.onModify,
			onDelete: options.onDelete
		}),
		document.getElementById(options.node));
}
