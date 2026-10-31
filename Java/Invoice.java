import java.util.ArrayList;
import java.util.List;

class Item {
   String name;
   int numberOfUnitsPurchased;
   double pricePerUnit;

   Item(String name, int numberOfUnitsPurchased, double pricePerUnit) {
      this.name = name;
      this.numberOfUnitsPurchased = numberOfUnitsPurchased;
      this.pricePerUnit = pricePerUnit;
   }

   public double makeTotalPrice() {
      return this.numberOfUnitsPurchased * this.pricePerUnit;
   }

   @Override
   public String toString() {
      return String.format("%s (%d x ₹%.2f = ₹%.2f)",
              this.name,
              this.numberOfUnitsPurchased,
              this.pricePerUnit,
              this.makeTotalPrice()
      );
   }
}

class Invoice {
   List<Item> orderList;
   double total, discount, tax, finalAmount;

   Invoice(List<Item> orderList, double total, double discount, double tax, double finalAmount) {
      this.orderList = orderList;
      this.total = total;
      this.discount = discount;
      this.tax = tax;
      this.finalAmount = finalAmount;
   }

   public String toString() {
      StringBuilder res = new StringBuilder();
      for (Item each : orderList)
         res.append(each).append("\n");
      res.append("Subtotal: ").append(this.total).append("\n")
              .append("Discounts: ").append(this.discount).append("\n")
              .append("Tax: ").append(this.tax).append("\n")
              .append("Total: ").append(this.finalAmount).append("\n");
      return res.toString();
   }
}

abstract class InvoiceMaker {
   abstract Invoice makeInvoice(List<Item> orderList, Discount D, int taxPercent);
}

class InvoiceProcessor extends InvoiceMaker {
   Invoice makeInvoice(List<Item> orderList, Discount D, int taxPercent) {
      double total = 0;
      for (Item each : orderList) {
         total += each.makeTotalPrice();
      }
      double discount = D.applyDiscount(total);
      double tax = (total - discount) * (taxPercent / 100.0D);
      double finalAmount = (total - discount) + tax;
      return new Invoice(orderList, total, discount, tax, finalAmount);
   }
}

abstract class Discount {
   abstract double applyDiscount(double total);
}

class PercentageDiscount extends Discount {
   double percentageOff;

   PercentageDiscount(double percentageOff) {
      this.percentageOff = percentageOff;
   }

   double applyDiscount(double total) {
      return total * (percentageOff / 100.0D);
   }
}

class FlatDiscount extends Discount {
   double flatOff;

   FlatDiscount(double flatOff) {
      this.flatOff = flatOff;
   }

   double applyDiscount(double total) {
      return flatOff;
   }
}

abstract class Notify {
   abstract void sendNotification(String message);
}

class EmailNotification extends Notify {
   String email;

   EmailNotification(String email) {
      this.email = email;
   }

   void sendNotification(String message) {
      System.out.println(message);
      System.out.println("E-mail successfully sent to user " + this.email);
   }
}

class SMSNotification extends Notify {
   String number;

   SMSNotification(String number) {
      this.number = number;
   }

   void sendNotification(String message) {
      System.out.println(message);
      System.out.println("Message successfully sent to user with Phone Number" + this.number);
   }
}

class InvoiceHandler {
   InvoiceHandler(InvoiceMaker I, List<Item> orderItems, Discount D, int taxPercent, Notify N) {
      Invoice invoice = I.makeInvoice(orderItems, D, 18);
      N.sendNotification(invoice.toString());
   }
}


class Main {
   public static void main(String[] args) {
      List<Item> orderList = new ArrayList<>();
      orderList.add(new Item("DDR5 16 GB 6000 MHZ CL30", 2, 25000));
      orderList.add(new Item("AMD Radeon 9070 XT 16 GB", 1, 100000));
      InvoiceHandler i = new InvoiceHandler(new InvoiceProcessor(), orderList, new PercentageDiscount(5), 18, new EmailNotification("ujjwal.a0001@gmai.com"));
   }
}



