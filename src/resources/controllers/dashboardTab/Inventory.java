package resources.controllers.dashboardTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resources.Main;
import resources.database.DatabaseConnection;
import resources.database.table.FoodMenuModel;
import resources.database.table.InventoryModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Manages items and food raw stock in inventory
 */
public class Inventory implements Initializable {
    @FXML
    private TableView<InventoryModel> table;

    @FXML
    private TableColumn<InventoryModel, String> amount, cost;

    @FXML
    private TableColumn<InventoryModel, String> description, name, type;

    private ObservableList<InventoryModel> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        String sql_validate = "SELECT * from inventory";

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql_validate);
            while (resultSet.next()){
                InventoryModel modelTable = new InventoryModel(
                        resultSet.getDouble("amount"),
                        resultSet.getDouble("cost"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("description"));

                this.observableList.add(modelTable);
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e){
                System.out.println(e);
            }
        }

        setCellValue();
        this.table.setItems(observableList);
    }

    private void setCellValue() {
        this.name.setCellValueFactory(new PropertyValueFactory<>("amount"));
        this.type.setCellValueFactory(new PropertyValueFactory<>("cost"));
        this.amount.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.cost.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.description.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    public static Stage newStage;

    @FXML
    void editItem() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/dashboardTab/dialogBox/inventory/Edit.fxml")));
        Scene scene = new Scene(root, Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        newStage = stage;
        stage.show();
    }

    @FXML
    void newItem() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("fxml/dashboardTab/dialogBox/inventory/New.fxml")));
        Scene scene = new Scene(root, Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        newStage = stage;
        stage.show();
    }
}
