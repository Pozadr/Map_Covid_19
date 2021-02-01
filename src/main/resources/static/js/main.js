$(document).ready(function () {
    $('.table .editButton').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (car, status) {
            console.log(car);
            $('#idEdit').val(car.id);
            $('#markEdit').val(car.mark);
            $('#modelEdit').val(car.model);
            $('#colorEdit').val(car.color);
            $('#dateEdit').val(car.productionDate);
        });

        $('#idEditMessage').hide();
        $('#markEditMessage').hide();
        $('#modelEditMessage').hide();
        $('#colorEditMessage').hide();
        $('#dateEditMessage').hide();

        $('#editModal').modal();
    });

    $('.table .deleteButton').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#deleteModal #idDelete').attr('href', href);
        $('#deleteModal').modal();
    });

});
