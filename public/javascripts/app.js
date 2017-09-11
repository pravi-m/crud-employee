var app = angular.module('empapp', ["ngRoute", "ui.bootstrap"]);

// Route configurations
app.config(['$routeProvider', function ($routeProvider){
$routeProvider.when('/employees', {
templateUrl: 'main.scala.html',
controller: 'empCtrl'
});
}]);

app.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);

}]);

app.config(['$httpProvider', function($httpProvider){

        $httpProvider.defaults.useXDomain = true;
}]);