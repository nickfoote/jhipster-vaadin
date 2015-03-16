'use strict';

angular.module('hippyApp')
    .factory('Password', function ($resource) {
        return $resource('/api/account/change_password', {}, {
        });
    });
