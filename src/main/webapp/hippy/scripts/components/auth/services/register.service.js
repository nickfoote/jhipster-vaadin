'use strict';

angular.module('hippyApp')
    .factory('Register', function ($resource) {
        return $resource('/api/register', {}, {
        });
    });


