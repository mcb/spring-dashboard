angular.module('clients', ['ngResource', 'ui.bootstrap']).
    factory('Clients', function ($resource) {
        return $resource('clients');
    }).
    factory('Client', function ($resource) {
        return $resource('clients/:id', {id: '@id'});
    }).
    factory("EditorStatus", function () {
        var editorEnabled = {};

        var enable = function (id, fieldName) {
            editorEnabled = { 'id': id, 'fieldName': fieldName };
        };

        var disable = function () {
            editorEnabled = {};
        };

        var isEnabled = function(id, fieldName) {
            return (editorEnabled['id'] == id && editorEnabled['fieldName'] == fieldName);
        };

        return {
            isEnabled: isEnabled,
            enable: enable,
            disable: disable
        }
    });

function ClientsController($scope, $modal, Clients, Client, Status) {
    function list() {
        $scope.clients = Clients.query();
    }

    function clone (obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    function saveClient(client) {
        Clients.save(client,
            function () {
                Status.success("Client saved");
                list();
            },
            function (result) {
                Status.error("Error saving client: " + result.status);
            }
        );
    }

    $scope.addClient = function () {
        var addModal = $modal.open({
            templateUrl: 'templates/clientForm.html',
            controller: ClientModalController,
            resolve: {
                client: function () {
                    return {};
                },
                action: function() {
                    return 'add';
                }
            }
        });

        addModal.result.then(function (client) {
            saveClient(client);
        });
    };

    $scope.updateClient = function (client) {
        var updateModal = $modal.open({
            templateUrl: 'templates/clientForm.html',
            controller: ClientModalController,
            resolve: {
                client: function() {
                    return clone(client);
                },
                action: function() {
                    return 'update';
                }
            }
        });

        updateModal.result.then(function (client) {
            saveClient(client);
        });
    };

    $scope.deleteClient = function (client) {
        Client.delete({id: client.id},
            function () {
                Status.success("Client deleted");
                list();
            },
            function (result) {
                Status.error("Error deleting client: " + result.status);
            }
        );
    };

    $scope.setClientsView = function (viewName) {
        $scope.clientsView = "templates/" + viewName + ".html";
    };

    $scope.init = function() {
        list();
        $scope.setClientsView("list");
        $scope.sortField = "name";
        $scope.sortDescending = false;
    };
}

function ClientModalController($scope, $modalInstance, client, action) {
    $scope.clientAction = action;
    $scope.yearPattern = /^[1-2]\d{3}$/;
    $scope.client = client;

    $scope.ok = function () {
        $modalInstance.close($scope.client);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

function ClientEditorController($scope, Clients, Status, EditorStatus) {
    $scope.enableEditor = function (client, fieldName) {
        $scope.newFieldValue = client[fieldName];
        EditorStatus.enable(client.id, fieldName);
    };

    $scope.disableEditor = function () {
        EditorStatus.disable();
    };

    $scope.isEditorEnabled = function (client, fieldName) {
        return EditorStatus.isEnabled(client.id, fieldName);
    };

    $scope.save = function (client, fieldName) {
        if ($scope.newFieldValue === "") {
            return false;
        }

        client[fieldName] = $scope.newFieldValue;

        Clients.save({}, client,
            function () {
                Status.success("Client saved");
                list();
            },
            function (result) {
                Status.error("Error saving client: " + result.status);
            }
        );

        $scope.disableEditor();
    };

    $scope.disableEditor();
}

angular.module('clients').
    directive('inPlaceEdit', function () {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,

            scope: {
                ipeFieldName: '@fieldName',
                ipeInputType: '@inputType',
                ipeInputClass: '@inputClass',
                ipePattern: '@pattern',
                ipeModel: '=model'
            },

            template:
                '<div>' +
                    '<span ng-hide="isEditorEnabled(ipeModel, ipeFieldName)" ng-click="enableEditor(ipeModel, ipeFieldName)">' +
                        '<span ng-transclude></span>' +
                    '</span>' +
                    '<span ng-show="isEditorEnabled(ipeModel, ipeFieldName)">' +
                        '<div class="input-append">' +
                            '<input type="{{ipeInputType}}" name="{{ipeFieldName}}" class="{{ipeInputClass}}" ' +
                                'ng-required ng-pattern="{{ipePattern}}" ng-model="newFieldValue" ' +
                                'ui-keyup="{enter: \'save(ipeModel, ipeFieldName)\', esc: \'disableEditor()\'}"/>' +
                            '<div class="btn-group btn-group-xs" role="toolbar">' +
                                '<button ng-click="save(ipeModel, ipeFieldName)" type="button" class="btn"><span class="glyphicon glyphicon-ok"></span></button>' +
                                '<button ng-click="disableEditor()" type="button" class="btn"><span class="glyphicon glyphicon-remove"></span></button>' +
                            '</div>' +
                        '</div>' +
                    '</span>' +
                '</div>',

            controller: 'ClientEditorController'
        };
    });
