'use strict';

angular.module('hippyApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
