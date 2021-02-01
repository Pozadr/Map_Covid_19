// validate filter by Country
let countryFilterByCountryError;
function validateFilterByCountry() {
    // from date
    let getByCountry = $('#getByCountry').val();
    if (getByCountry.length === 0) {
        $('#getByCountryMessage').show();
        countryFilterByCountryError = true;
    } else {
        $('#getByCountryMessage').hide();
        countryFilterByCountryError = false;
    }

    return !(countryFilterByCountryError);
}

// validate getHistory
let fromFilterByDateError;
let toFilterByDateError;
function validateGetHistory() {
    // from date
    let fromFilterByDate = $('#fromDateFilterByDate').val();
    if (fromFilterByDate.length === 0) {
        $('#fromDateFilterByDateMessage').show();
        fromFilterbyDateError = true;
    } else if (!(isValidDate(fromFilterByDate))) {
        $('#fromDateFilterByDateMessage').html
        ("**should be date format: yyyy-mm-dd");
        $('#fromDateFilterByDateMessage').css("color", "red");
        fromFilterbyDateError = true;
    } else {
        $('#fromDateFilterByDateMessage').hide();
        fromFilterbyDateError = false;
    }
    // to date
    let toFilterByDate = $('#toDateFilterByDate').val();
    if (toFilterByDate.length === 0) {
        $('#toDateFilterByDateMessage').show();
        toFilterbyDateError = true;
    } else if (!(isValidDate(toFilterByDate))) {
        $('#toDateFilterByDateMessage').html
        ("**should be date format: yyyy-mm-dd");
        $('#toDateFilterByDateMessage').css("color", "red");
        toFilterbyDateError = true;
    } else {
        $('#toDateFilterByDateMessage').hide();
        toFilterbyDateError = false;
    }

    return !(fromFilterbyDateError || toFilterbyDateError);
}


// Submit filterByCountry
$('#submitFilterByCountry').click(function () {
    return validateFilterByCountry();
});

// Submit getHistory
$('#submitGetHistory').click(function () {
    return validateGetHistory();
});


// #filterByCountryButton on click
$('#filterByCountryButton').click(function () {
    $('#getByCountryMessage').hide();
});

// getHistoryButton on click
$('#getHistoryButton').click(function () {
    $('#getHistoryCountryMessage').hide();
    $('#fromDateGetHistoryMessage').hide();
    $('#toDateGetHistoryMessage').hide();
});
