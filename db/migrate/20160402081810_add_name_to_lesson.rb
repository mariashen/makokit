class AddNameToLesson < ActiveRecord::Migration
  def change
    add_column :lessons, :name, :string
    add_column :lessons, :version, :integer
  end
end
