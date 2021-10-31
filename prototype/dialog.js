function toList(data)
{
	let key, value, i, length;
	var array = Object.entries(data);
	let result = "<ul>";
	
	length = array.length;
	for(i=0; i<length; i++)
	{
		key = array[i][0];
		value = array[i][1];
		if (value instanceof Object)
			value = toList(value);
		result += "<li>"+key+":"+value;
	}
	result += "</ul>"

	return result;
}


function Data(tableData, dataScheme)
{
	if ((tableData == null) || (dataScheme == null))
		alert("constructor Data("+tableData+","+dataScheme+")");
	this.tableData = tableData;
	this.dataScheme = dataScheme;
	this.inserts = {};
	this.updates = {};
	this.deletes = {};
}
Data.prototype.get = new function(index, column)
{
	var row= tableData[index];
	var data= tableData[column];
	return data;
}
Data.prototype.set = new function(index, column, value)
{
	var row= tableData[index];
	var data= tableData[column];
	if (data != value)
	{
		rowInserts = this.inserts[index];
	}
}
Data.prototype.getIndexes = new function()
{
	
}
Data.prototype.getColumns = new function()
{
	alert("here 2");
	
	var objColumns = this.dataScheme.columns;
	var arrayColumns = Object.entries(objColumns);
	var retValue = [];
	let i, length, item, key;

	

	length = arrayColumns.length;
	for(i=0; i<length; i++)
	{
		key = arrayColumns[i][0];
		//item = arrayColumns[i][1];
		retValue[i] = key;
	}
	return retValue;
}



function tableDialog(descDialog, tableData)
{
	var retValue = "";
	let i, length, column;
	
	retValue = toList(tableData);
	
	return retValue;
}

function insertDialog(descDialog, tableData)
{
    return "<h1>Insert Dialog here</h1>";
}

function chooseDialog(descDialog, tableData)
{
	var retValue = "";
	let i, length, column;
	
	retValue = toList(tableData);
	
	return retValue;
}