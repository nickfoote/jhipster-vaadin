'use strict';

angular.module('hippyApp')
    .factory('Activate', function ($resource) {
        return $resource('/api/activate', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });


