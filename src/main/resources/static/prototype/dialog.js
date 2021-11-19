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
	if (tableData == null)
		tableData = {};
	if (dataScheme == null)
		dataScheme = {};

	this.tableData = tableData;
	this.dataScheme = dataScheme;
	this.inserts = {};
	this.updates = {};
	this.deletes = {};
};
Data.prototype.get = function(index, column)
{
	var row= this.tableData[index];
	let data= row[column];
	//alert(index+":"+column+"="+data);
	return data;
};
Data.prototype.getRow = function(index)
{
	var row= this.tableData[index];
	return row;
};
Data.prototype.set = function(index, column, value)
{
	var row= tableData[index];
	var data= tableData[column];
	if (data != value)
	{
		rowInserts = this.inserts[index];
	}
};
Data.prototype.getIndexes = function()
{
	if (this.tableData == null)
		this.tableData = {};
	var objIndexes = this.tableData;
	var arrayIndexes = Object.entries(objIndexes);
	
	var retValue = [];
	let i, length, item, key;

	length = arrayIndexes.length;
	for(i=0; i<length; i++)
	{
		key = arrayIndexes[i][0];
		//item = arrayColumns[i][1];
		retValue[i] = key;
	}
	return retValue;
};
Data.prototype.getColumns = function()
{
	if (this.dataScheme.columns == null)
		this.dataScheme.columns = {};
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
};
Data.prototype.getForeignKeys = function()
{
	//alert("here"+toList(this.dataScheme.foreignkeys));
	if (this.dataScheme.foreignkeys == null)
		this.dataScheme.foreignkeys = {}
	var objForeignKeys = this.dataScheme.foreignkeys;
	var arrayForeignKeys = Object.entries(objForeignKeys);
	return arrayForeignKeys;
};



function tableDialog(descDialog, data)
{
	var retValue = "";
	var arrayData = null;
	let iLength, i, jLength, j, name, index, k;
	
	if (descDialog == null) // programmer gave no dialogdescription
		descDialog= {};

	//get the primaryTable
	//saves the primary table name in sPrimaryTable
	//saves the primary table in dPrimaryTable
	if (descDialog.primaryTable == null) // programmer gave no primarytable in dialogdescription
	{
		arrayData = Object.entries(data);
		if (arrayData.length > 0)
			descDialog.primaryTable = arrayData[0][0];
		else
			descDialog.primaryTable = "";
	}
	let sPrimaryTable = descDialog.primaryTable;
	var dPrimaryTable = data[sPrimaryTable];
	if (dPrimaryTable == null)
		alert("Can't find primary table "+sPrimaryTable);
	
	//get the foreignTables	
	//saves the foreign tables name in sForeignTables (an array)
	//saves the foreign tables in dForeignTables (an array)
	if (descDialog.foreignTables == null) // programmer gave no foreigntable in dialogdescription
	{
		if (arrayData == null)
			arrayData = Object.entries(data);
		descDialog.foreignTables = {};
		if (arrayData.length > 1)
		{
			iLength = arrayData.length;
			for (i = 1; i<iLength; i++)
			{
				descDialog.foreignTables[arrayData[i][0]] = "";
			}
		}
	}
	var sForeignTables= Object.entries(descDialog.foreignTables);
	var dForeignTables= [];
	iLength = sForeignTables.length;
	for (i = 0; i<iLength; i++)
	{
		name = sForeignTables[i][0];
		dForeignTables[i] = data[name];
		if (dForeignTables[i] == null)
			alert("Can't find foreign table "+name);
	}
	
	//now we have primaryTable and foreignTables
    
	//the table header
	retValue += "<table border='1'><tr>";
	var colsPrimaryTable = dPrimaryTable.getColumns();
	iLength = colsPrimaryTable.length;
	for (i= 0; i<iLength; i++)
		retValue += "<th>"+colsPrimaryTable[i]+"</th>";
	var fksPrimaryTable = dPrimaryTable.getForeignKeys();
	iLength = fksPrimaryTable.length;
	for (i= 0; i<iLength; i++)
		retValue += "<th>"+fksPrimaryTable[i][0]+"</th>";
	retValue += "</tr>";
	
	//the table columns
	var indexes = dPrimaryTable.getIndexes();
	var column, refered;
	let html, sReferedColumn, sReferedTable;
	iLength = indexes.length;
	for(i = 0; i < iLength; i++)
	{
		retValue += "<tr>";
		index = indexes[i];
		jLength = colsPrimaryTable.length;
		for (j = 0; j < jLength; j++)
		{
			column = colsPrimaryTable[j];
			retValue += "<td>"+dPrimaryTable.get(index, column)+"</td>";
		}
		jLength = fksPrimaryTable.length;
		for (j = 0; j < jLength; j++)
		{
			column = fksPrimaryTable[j];
			value = dPrimaryTable.get(index, column[0]);
			referingData = 
			{
				"index":index,
				"column":column,
				"value":value
			}
			refered = column[1]
			sReferedTable = refered.table;
			sReferedColumn = refered.column;
			//alert(sReferedTable+","+sReferedColumn+","+sForeignTables);
			
			for(k = 0;k < sForeignTables.length; k++)
			{
				//alert(sForeignTables[k][0]+" == "+sReferedTable);
				if (sForeignTables[k][0].toUpperCase() == sReferedTable.toUpperCase())
					break;
			}
			if (k < sForeignTables.length)
			{
				refered = dForeignTables[k];				
				refered = refered.getRow(value);
			}
			else
				alert("refered table "+sReferedTable+" not found.");
			html = value;
			if ((sForeignTables[k][1] != null) && (sForeignTables[k][1].html != null))
				html = sForeignTables[k][1].html(refered)
			retValue += "<td>"+html+"</td>";
		}
		retValue += "</tr>";
	}

	retValue += "</table>";

	retValue += toList(data);
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