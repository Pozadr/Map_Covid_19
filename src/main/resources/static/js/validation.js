// validate filter by Country
let countryFilterByCountryError;
function validateFilterByCountry() {
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
let countryGetHistoryError;
function validateGetHistory() {
    let getHistory = $('#getHistory').val();
    if (getHistory.length === 0) {
        $('#getHistoryCountryMessage').show();
        countryGetHistoryError = true;
    } else {
        $('#getHistoryCountryMessage').hide();
        countryGetHistoryError = false;
    }
    return !(countryGetHistoryError);
}


// Submit filterByCountry
$('#submitFilterByCountry').click(function () {
    return validateFilterByCountry();
});

// Submit getHistory
$('#submitGetHistory').click(function () {
    return validateGetHistory();
});


// filterByCountryButton on click
$('#filterByCountryButton').click(function () {
    $('#getByCountryMessage').hide();
});

// getHistoryButton on click
$('#getHistoryButton').click(function () {
    $('#getHistoryCountryMessage').hide();
    $('#fromDateGetHistoryMessage').hide();
    $('#toDateGetHistoryMessage').hide();
});
