"use strict";
app.controller("empCtrl", function($scope, $http, $uibModal) {
    //$scope.test="test message";

    $scope.employees={};
    $scope.editMode=[];

    $scope.fetch = function(){
        $http({method: 'GET',url: '/employees'}).then(function(response){
            console.log("Data" + response, 'res');
            $scope.employees=response.data;
            }, function(error){
            console.log(error,'cant get data');
        });
        };
    $scope.fetch();
    $scope.selected={};

    $scope.cancelInsert =function(){
        $scope.employee={};
    };

    // to edit an employee record
    $scope.editEmployee = function (employee, index) {
         $scope.cancelInsert();
         $scope.editMode[index]=true;
         $scope.selected = angular.copy(employee);
    };

    // cancel selected record
    $scope.cancel = function (index) {
        $scope.editedEmployee = $scope.selected;
        $scope.editMode[index]=false;
        $scope.fetch();
    };

    // to add a new employee
    $scope.AddEmployee = function(employee){
        $scope.submitted=true;
        $scope.initRec=employee;
        $scope.employee=angular.copy(employee);
        //$scope.employee.id='';
        var emp= $scope.employee;
        console.log('POST', emp);
        $http({
                method: 'POST',
                url: '/employees',
                withCredentials: true,
                headers: {'Content-Type': 'application/json',
                'Access-Control-Allow-Headers': 'Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With',
                 'Access-Control-Allow-Origin':'*'
                 },
                data: emp
             }).then( function(result){
                    if (result.status==200|| result.status==201|| result.status==202)
                    {
                        $scope.init;
                        $scope.employees.push(emp);
                        $scope.employee={};
                        $scope.fetch();
                        console.log("record added");
                    }
                    else {
                        console.log("Error: " , result);
                        alert('Error adding Employee');
                    }
            });
        };

    // to update an edited employee record
    $scope.updateEmployee =function(employee, index){
        $scope.editMode[index]=false;
        $scope.initRec=employee;
        $scope.editedEmployee=angular.copy(employee);
        var emp= $scope.editedEmployee;
        $http({
            method:'PUT',
            url: '/employees/'+emp._id.$oid,
            withCredentials: true,
                             headers: {'Content-Type': 'application/json',
                             'Access-Control-Allow-Headers': 'Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With',
                              'Access-Control-Allow-Origin':'*'
                              },
            data: emp}).then(function(result){
                if (result.status==200|| result.status==201|| result.status==202)
                {
                     angular.copy($scope.editedEmployee, $scope.initRec);
                     $scope.fetch();
                     console.log("record updated")
                }
                else {
                        console.log("Error: " , result);
                        alert('Error updating Employee');
                    }
            });
    };

    // to delete an employee
    $scope.deleteEmployee = function(employee){
        $scope.cancelInsert();
        var modalInstance = $uibModal.open({
              templateUrl: 'assets/views/empModal.html',
              controller: empModalCtrl});
        //var dialogResult = confirm("Are you sure?");
        modalInstance.result.then(function(){
            $scope.deletedEmployee = angular.copy(employee)
            var emp = $scope.deletedEmployee;
            console.log('DELETE ');
            $http({
                method:'DELETE',
                url: '/employees/'+ emp._id.$oid,
                withCredentials: true,
                headers: {'Content-Type': 'application/json',
                                             'Access-Control-Allow-Headers': 'Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With',
                                              'Access-Control-Allow-Origin':'*'
                                              },
                data: emp}).then(function(result){
                console.log(result)
                if (result.status==200|| result.status==201|| result.status==202){
                     var index = $scope.employees.indexOf(emp);
                     if (index > -1) {$scope.employees.splice(index, 1);}
                     $scope.fetch();
                     console.log("Employee Record Deleted")
                }
                else {
                        console.log("Error: " , result);
                        alert('Error deleting Employee');
                    }
                });
            })
        };
});

var empModalCtrl = function ($scope, $uibModalInstance) {

  $scope.ok = function () {
  $uibModalInstance.close();
  };

  $scope.cancel = function () {
    $uibModalInstance.dismiss('cancel');
  };
};