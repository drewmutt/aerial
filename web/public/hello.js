var app = angular.module("app",["ngResource"]);

/*
var Users = $resource('/user/:userId', { userId: '@id' });
var user = Users.get({ userId: 123 }, function () {
    user.abc = true;
    //user.$save();
});
*/


app.factory('RadioStations', function ($resource) {
    return $resource('http://192.168.2.2:8080/aerial/getstations', {});
});

/*
.controller('TasklistCtrl', function ($scope, Tasks) {

    $scope.taskList = Tasks.query();

    //$scope.addTask = function(){

        //Tasks.save($scope.newTask, function(data) {
        //    $scope.taskList.push(data);
        //});

}
*/

//getfrequency


app.controller('Hello', ['$scope', '$http', '$timeout', 'RadioStations', function($scope, $http, $timeout, radiostations)
{
    var promise = radiostations.query().$promise;



    $scope.getMoreInfo = function(item) {

    };

    $scope.onTimeout = function(){
        $http.get('http://192.168.2.2:8080/aerial/getfrequency').
            success(function(data) {
                $scope.currentFreq = data;

                //console.log($scope.greeting);

                var closestStation;
                var closestStationDistance = 1000;
                for(var i = 0; i < $scope.stationList.length; i++)
                {
                    var dist = Math.abs($scope.stationList[i].frequency - $scope.currentFreq);
                    //console.log(dist);
                    if(dist < closestStationDistance)
                    {
                        closestStation = $scope.stationList[i];
                        closestStationDistance = dist;
                    }
                }
                $scope.currentStation = closestStation;
            });

        mytimeout = $timeout($scope.onTimeout,1000);

    };


    promise.then(function (data) {
        $scope.stationList = data;
        $scope.onTimeout();
    }, function () {

    });


    $scope.stop = function(){
        $timeout.cancel(mytimeout);
    }
}]);

/*
var RadioStation = $resource('http://192.168.2.2:8080/aerial/getStations',
     {
        //charge: {method:'POST', params:{charge:true}}
    });

//var RadioStation = $resource('http://192.168.2.2:8080/aerial/getStations',
//    {userId:123, cardId:'@id'}, {
//        charge: {method:'POST', params:{charge:true}}
//    });

// We can retrieve a collection from the server
var cards = RadioStation.query(function() {
    // GET: /user/123/card
    // server returns: [ {id:456, number:'1234', name:'Smith'} ];

    var card = cards[0];
    // each item is an instance of RadioStation
    expect(card instanceof RadioStation).toEqual(true);
    //card.name = "J. Smith";
    console.out("Got stuff!");

    // non GET methods are mapped onto the instances
    //card.$save();


    // POST: /user/123/card/456 {id:456, number:'1234', name:'J. Smith'}
    // server returns: {id:456, number:'1234', name: 'J. Smith'};

    // our custom method is mapped as well.
    //card.$charge({amount:9.99});
    // POST: /user/123/card/456?amount=9.99&charge=true {id:456, number:'1234', name:'J. Smith'}
});
*/
/*==
app.factory("Post", function($resource) {
    return $resource("/api/posts/:id");
});


/*
myApp.controller('Hello', ['$scope', '$http', function($scope, $http)
{
    $http.get('http://192.168.2.2:8080/aerial/getplaylists').
        success(function(data)
        {
            $scope.greeting = data;
        });

}]);
*/
//angular.module('myApp.controllers',[]);
//
//angular.module('myApp.controllers').controller('ResourceController',function($scope, Entry)
//{
//    var entry = Entry.get({ id: $scope.id }, function() {
//        console.log(entry);
//    }); // get() returns a single entry
//
//    var entries = Entry.query(function() {
//        console.log(entries);
//    }); //query() returns all the entries
//
//    $scope.entry = new Entry(); //You can instantiate resource class
//
//    $scope.entry.data = 'some data';
//
//    Entry.save($scope.entry, function() {
//        //data saved. do something here.
//    }); //saves an entry. Assuming $scope.entry is the Entry object
//});