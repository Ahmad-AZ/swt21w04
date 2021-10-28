const dataScheme=
{
	adresses:
	{
		primarykey:
		{
			idAddress:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:45
			},
			street:
			{
				type:"varchar",
				length:45
			},
			zip:
			{
				type:"varchar",
				length:5
			},
			city:
			{
				type:"varchar",
				length:45
			},
			country:
			{
				type:"varchar",
				length:45
			}
		}
	},
	locations:
	{
		primarykey:
		{
			idLocation:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:45
			},
			cntVisitors:
			{
				type:"int"
			},
			cntStages:
			{
				type:"int"
			}
		},
		foreignkeys:
		{
			idAddress:
			{
				table:"Addresses",
				column:"idAddress"
			}
		}
	},
	locationAreas:
	{
		primarykey:
		{
			idLocation:"idLocationArea"
		},
		columns:
		{
			locked:
			{
				type:"int",
				choice:
				{
					1:"locked",
					2:"unlocked"
				}
			},
			kind:
			{
				type:"int",
				choice:
				{
					1:"camping",
					2:"park",
					1:"catering",
					2:"stage"
				}
			},
			cntVisitors:
			{
				type:"int"
			},
			label:
			{
				type:"varchar",
				length:45
			}
		},
		foreignkeys:
		{
			idLocation:
			{
				table:"Locations",
				column:"idLocation"
			}
		}
	},
	festivals:
	{
		primarykey:
		{
			idFestival:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:45
			},
			begin:
			{
				type:"datetime"
			},
			end:
			{
				type:"datetime"
			}
		},
		foreignkeys:
		{
			idLocation:
			{
				table:"Locations",
				column:"idLocation"
			}
		}
	},
	workers:
	{
		primarykey:
		{
			idWorker:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:80
			},
			email:
			{
				type:"varchar",
				length:120
			},
			qualification:
			{
				type:"varchar",
				length:120
			}				
		},
		foreignkeys:
		{
			idAddress:
			{
				table:"Addresses",
				column:"idAddress"
			}
		}
	},
	employments:
	{
		primarykey:
		{
			idEmployment:"int"
		},
		columns:
		{
			begin:
			{
				type:"datetime"
			},
			end:
			{
				type:"datetime"
			},
			jobtime:
			{
				type:"int"
			},
			loan:
			{
				type:"int"
			},
			login:
			{
				type:"varchar",
				length:45
			},
			password:
			{
				type:"varchar",
				length:45
			},
			usergroup:
			{
				type:"int",
				choice:
				{
					1:"developer",
					2:"agency",
					3:"manager",
					4:"conductor",
					5:"catering",
					6:"stage",
					7:"security",
					8:"tickets"
				}
			}
		},
		foreignkeys:
		{
			idWorker:
			{
				table:"Workers",
				column:"idWorker"
			},
			idFestival:
			{
				table:"Festivals",
				column:"idFestival"
			}
		}
	},
	bands:
	{
		primarykey:
		{
			idBand:"int"
		},
		columns:
		{
			bandname:
			{
				type:"varchar",
				length:45
			},
			email:
			{
				type:"varchar",
				length:120
			},
			cntMembers:
			{
				type:"int"
			}
		},
		foreignkeys:
		{
			idAddress:
			{
				table:"Addresses",
				column:"idAddress"
			}
		}
	},
	performances:
	{
		primarykey:
		{
			idPerformance:"int"
		},
		columns:
		{
			price:
			{
				type:"double"
			}
		},
		foreignkeys:
		{
			idAddress:
			{
				table:"Bands",
				column:"idBand"
			},
			idFestival:
			{
				table:"Festivals",
				column:"idFestival"
			},
			idStage:
			{
				table:"LocationAreas",
				column:"idStage"
			}	
		}
	},
	suppliers:
	{
		primarykey:
		{
			idSupplier:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:45
			},
			email:
			{
				type:"varchar",
				length:120
			}
		},
		foreignkeys:
		{
			idAddress:
			{
				table:"Addresses",
				column:"idAddress"
			}
		}
	},
	products:
	{	
		primarykey:
		{
			idSupplier:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:45
			},
			outprice:
			{
				type:"double"
			},
			forsale:
			{
				type:"tinyint",
				choice:
				{
					1:"for sale",
					2:"not for sale"
				}
			}
		},
		foreignkeys:
		{
			idSupplier:
			{
				table:"Suppliers",
				column:"idSupplier"
			}
		}
	},
	orders:
	{	
		primarykey:
		{
			idOrder:"int"
		},
		columns:
		{
			inprice:
			{
				type:"double"
			},
			amount:
			{
				type:"int"
			},
			orderDate:
			{
				type:"datetime"
			}
		},
		foreignkeys:
		{
			idProduct:
			{
				table:"Products",
				column:"idProduct"
			},
			idFestival:
			{
				table:"Festival",
				column:"idFestival"
			}
		}
	},
	sales:
	{	
		primarykey:
		{
			idSale:"int"
		},
		columns:
		{
			price:
			{
				type:"double"
			},
			amount:
			{
				type:"int"
			},
		},
		foreignkeys:
		{
			idProduct:
			{
				table:"Products",
				column:"idProduct"
			},
			idFestival:
			{
				table:"Festival",
				column:"idFestival"
			}
		}
	},
	materials:
	{
		primarykey:
		{
			idMaterial:"int"
		},
		columns:
		{
			name:
			{
				type:"varchar",
				length:45
			},
			height:
			{
				type:"float",
			},
			depth:
			{
				type:"float",
			},
			width:
			{
				type:"float",
			}
		},
		foreignkeys:
		{
			idSupplier:
			{
				table:"Suppliers",
				column:"idSupplier"
			}
		}
	},
	rentals:
	{
		primarykey:
		{
			idMaterial:"int"
		},
		columns:
		{
			begin:
			{
				type:"datetime"
			},
			end:
			{
				type:"datetime"
			},
			rentalPrice:
			{
				type:"double"
			}
		},
		foreignkeys:
		{
			idFestival:
			{
				table:"Festivals",
				column:"idFestival"
			},
			idMaterial:
			{
				table:"Materials",
				column:"idMaterial"
			},
			idLocationArea:
			{
				table:"LocationAreas",
				column:"idLocationArea"
			}
		}
	}
}
if (debug) document.writeln("data scheme okay+<br>");