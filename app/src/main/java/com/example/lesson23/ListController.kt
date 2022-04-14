package com.example.lesson23

class ListController {
    private var view: ListView? = null

    fun onViewReady(view: ListView) {
        this.view = view
        view.displayList(UserRepository.users)


        if (!PermissionChecker.hasWriteExternalStoragePermission()) {
            view.askForWriteExternalStoragePermission()
        }
    }

    fun onViewDestroyed() {
        this.view = null
    }

    fun onRemoveAllClicked() {
        UserRepository.removeAll()
        view?.displayList(UserRepository.users)
    }

    fun onRemoveLastClicked() {
        UserRepository.removeLast()
        view?.displayList(UserRepository.users)
    }

    fun onEditSecondClicked() {
        if (UserRepository.users.size < 2) return

        UserRepository.updateUser(1, User("SECOND", "SECOND", "test test tesst"))
        view?.displayList(UserRepository.users)
    }

    fun onAddRandomClicked() {
        UserRepository.addRandomUser()
        view?.displayList(UserRepository.users)
    }


}