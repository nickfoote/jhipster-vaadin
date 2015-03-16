'use strict';

angular.module('hippyApp')
    .factory('Book', function ($resource) {
        return $resource('/api/books/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    var pubDateFrom = data.pubDate.split("-");
                    data.pubDate = new Date(new Date(pubDateFrom[0], pubDateFrom[1] - 1, pubDateFrom[2]));
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
