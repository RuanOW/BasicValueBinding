import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import tornadofx.*

class MainBank: View("Welcome") {

    val accountList = mutableListOf(
        Account("John", "Tax Free Savings", 12.0f),
        Account("Jane", "Easy Savings", 24.0f),
    )

    override val root = hbox {
        spacing = 20.0
        for (acc in accountList) {
            add(AccountView(AccountViewModel(acc)))

        }
    }
}

// Account Model
class Account(firstName: String, type: String, balance: Float) {
    val firstNameProperty = SimpleStringProperty(this, "firstName", firstName)
    val typeProperty = SimpleStringProperty(this, "type", type)
    val balanceProperty = SimpleFloatProperty(this,"balance", balance)
}

// Account View Model
class AccountViewModel(val account: Account): ViewModel() {
    val firstName = bind { account.firstNameProperty }
    val type = bind { account.typeProperty }
    val balance = bind { account.balanceProperty }

    fun deposit(amount: Float) {
       balance.value += amount
    }

    fun withdraw(amount: Float): Float {
        if ((balance - amount) >= 0){
            balance.value -= amount
            return balance.value
        } else {
            return -1f
        }
    }

//    fun applyTransactionFee(){
//        if (type == "Gold Cheque") {
//            balance += 15f
//        } else if (type == "Diamond Cheque") {
//            balance += 20f
//        }
//
//    }

    fun applyInterest(){
        if (type.value == "Tax Free Savings") {
            val interest = balance * 0.12
            balance.value += interest.value.toFloat()
        } else if (type.value == "Easy Savings") {
            val interest = balance * 0.8
            balance.value += interest.value.toFloat()
        } else {
            balance.plus(balance * 1)
        }
    }
}

class AccountView(val accountViewModel: AccountViewModel): View() {

    val depositAmount = SimpleFloatProperty()
    val withDrawnAmount = SimpleFloatProperty()

    override val root = vbox {
        spacing = 10.0
        label("First Name: ${ accountViewModel.firstName.value }")
        label("Current Balanance: ${ accountViewModel.balance.value }") {
            accountViewModel.balance.onChange {
                text = "Current Balanance: $it"
            }
        }

        label("Withdrawn Amount: ${ withDrawnAmount.value }") {
            withDrawnAmount.onChange {
                text = "Withdrawn Amount: $it"
            }
        }

        form {
            fieldset {
                field ("Deposit Amount") {
                    textfield(depositAmount)
                }
                button("deposit") {
                    action {
                        accountViewModel.deposit(depositAmount.value)
                        println("New Balance: ${accountViewModel.balance.value}")
                    }
                }
                field ("Withdraw Amount") {
                    textfield(withDrawnAmount)
                }
                button("Withdraw") {
                    action {
                        val withdrawn = accountViewModel.withdraw(withDrawnAmount.value)
                        println("Withdrawal: ${withdrawn}")
                    }
                }
            }
        }
        button("Apply Interest") {
            action {
                accountViewModel.applyInterest()
                println("Balance after Interest: ${accountViewModel.balance.value}")
            }
        }


    }
}
