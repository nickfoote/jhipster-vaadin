'use strict';

angular.module('hippyApp')
    .factory('Sessions', function ($resource) {
        return $resource('/api/account/sessions/:series', {}, {
            'getAll': { method: 'GET', isArray: true}
        });
    });



