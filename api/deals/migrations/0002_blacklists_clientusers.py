# Generated by Django 3.0.3 on 2020-02-27 13:10

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('deals', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='ClientUsers',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=155)),
                ('uid', models.CharField(max_length=155)),
            ],
        ),
        migrations.CreateModel(
            name='BlackLists',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('clientuser1', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='clientuser1', to='deals.ClientUsers')),
                ('clientuser2', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='clientuser2', to='deals.ClientUsers')),
                ('deal', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='deals.Deals')),
            ],
        ),
    ]
