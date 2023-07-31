
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}
//Converting to CSV
function convertToCSV(objArray) {
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    var str = '';

    for (var i = 0; i < array.length; i++) {
        var line = '';
        for (var index in array[i]) {
            if (line != '') line += ','

            line += array[i][index];
        }

        str += line + '\r\n';
    }

    return str;
}
//Download CSV Helper function
function exportCSVFile(headers, items, fileTitle) {
    if (headers) {
        items.unshift(headers);
    }

    // Convert Object to JSON
    var jsonObject = JSON.stringify(items);

    var csv = this.convertToCSV(jsonObject);

    var exportedFilenmae = fileTitle + '.csv' || 'export.csv';

    var blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    if (navigator.msSaveBlob) { // IE 10+
        navigator.msSaveBlob(blob, exportedFilenmae);
    } else {
        var link = document.createElement("a");
        if (link.download !== undefined) { // feature detection
            // Browsers that support HTML5 download attribute
            var url = URL.createObjectURL(blob);
            link.setAttribute("href", url);
            link.setAttribute("download", exportedFilenmae);
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
    }
}

function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
//	alert(response.message);
	dangerClick(response.message);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
		    if(hasDuplicateHeaders(results.meta.fields)){
		    warnClick("Duplicate headers not supported");
		    return;
		    }
		    var headers = Object.keys(results.data[0]);
            for( var i in headers){
                if(headers[i]==null || headers[i]==""){
                    warnClick("File should not contain empty headers");
                    return;
                }
            }
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}
function hasDuplicateHeaders(headers) {
  const seenHeaders = {};

  // Iterate through each header
  for (const header of headers) {
    // If the header already exists in the seenHeaders object, it's a duplicate
    if (seenHeaders[header]) {
      return true; // Return true if duplicates found
    } else {
      // Otherwise, mark the header as seen
      seenHeaders[header] = true;
    }
  }

  return false; // Return false if no duplicates found
}

function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}

function writeReportData(arr, name){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, name);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', name);
    tempLink.click();
}

//Notify.js
function dangerClick(alertMessage){

//  $.notify(alertMessage, { position:"top-center",
//                            // whether to hide the notification on click
//                              clickToHide: true,
//                              // whether to auto-hide the notification
//                              autoHide: false,
//                              showAnimation: 'slideDown',
//                              // show animation duration
//                              showDuration: 400,
//                              // hide animation
//                              hideAnimation: 'slideUp',
//                              closeOnCrossClick: true,
//                              className: 'error'
//   });
   Swal.fire({
     icon: 'error',
     title: 'Oops...',
     text: alertMessage
   })
}
function warnClick(alertMessage){
   $.notify(alertMessage, { position:"top-center",
                              // whether to hide the notification on click
                                clickToHide: true,
                                // whether to auto-hide the notification
                                autoHide: true,
                                // if autoHide, hide after milliseconds
                                autoHideDelay: 5000,
                                showAnimation: 'slideDown',
                                // show animation duration
                                showDuration: 400,
                                // hide animation
                                hideAnimation: 'slideUp',
                                className: 'warn'
     });
}
function successClick(alertMessage){
   $.notify(alertMessage, { position:"top-center",
                              // whether to hide the notification on click
                                clickToHide: true,
                                // whether to auto-hide the notification
                                autoHide: true,
                                // if autoHide, hide after milliseconds
                                autoHideDelay: 5000,
                                showAnimation: 'slideDown',
                                // show animation duration
                                showDuration: 400,
                                // hide animation
                                hideAnimation: 'slideUp',
                                className: 'success'
     });
}